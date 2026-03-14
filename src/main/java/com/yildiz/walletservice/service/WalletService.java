package com.yildiz.walletservice.service;

import com.yildiz.walletservice.entity.Wallet;
import com.yildiz.walletservice.exception.WalletNotFoundException;
import com.yildiz.walletservice.repository.WalletRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final StringRedisTemplate redisTemplate;
    private final KafkaProducerService kafkaProducerService; // 1. Önce alanı tanımlıyoruz

    // 2. Constructor (Kurucu) içine ekliyoruz
    public WalletService(WalletRepository walletRepository,
                         StringRedisTemplate redisTemplate,
                         KafkaProducerService kafkaProducerService) {
        this.walletRepository = walletRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaProducerService = kafkaProducerService; // 3. Atamayı yapıyoruz
    }

    public Wallet createWallet(String email, BigDecimal initialBalance) {
        Wallet wallet = new Wallet();
        wallet.setUserEmail(email);
        wallet.setBalance(initialBalance);
        wallet.setCurrency("TRY");
        Wallet savedWallet = walletRepository.save(wallet);

        // Yeni cüzdan oluşunca Redis'e de yazalım
        redisTemplate.opsForValue().set("wallet:" + email, initialBalance.toString(), Duration.ofMinutes(30));

        return savedWallet;
    }

    public Wallet getWalletByEmail(String email) {
        // 1. Önce Redis'e sor
        String cachedBalance = redisTemplate.opsForValue().get("wallet:" + email);

        if (cachedBalance != null) {
            System.out.println("DEBUG: Bakiye Redis'ten getirildi!");
            Wallet wallet = new Wallet();
            wallet.setUserEmail(email);
            wallet.setBalance(new BigDecimal(cachedBalance));
            wallet.setCurrency("TRY");
            return wallet;
        }

        // 2. Redis'te yoksa DB'ye git
        System.out.println("DEBUG: Bakiye DB'den getirildi!");
        Wallet wallet = walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Cüzdan bulunamadı!"));

        // 3. Bulduğun veriyi Redis'e de yaz (Bir dahaki sefere hızlı gelsin)
        redisTemplate.opsForValue().set("wallet:" + email, wallet.getBalance().toString(), Duration.ofMinutes(30));

        return wallet;
    }

    @Transactional
    public void transferMoney(String fromEmail, String toEmail, BigDecimal amount) {
        // transferMoney metodunun içindeki orElseThrow kısımlarını böyle güncelle:
        Wallet fromWallet = walletRepository.findByUserEmail(fromEmail)
                .orElseThrow(() -> new WalletNotFoundException("Gönderen cüzdan bulunamadı: " + fromEmail));

        Wallet toWallet = walletRepository.findByUserEmail(toEmail)
                .orElseThrow(() -> new WalletNotFoundException("Alıcı cüzdan bulunamadı: " + toEmail));

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Yetersiz bakiye!");
        }

        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        // KRİTİK: Transfer bittiğinde Redis'teki ESKİ verileri temizle
        // Bir sonraki sorguda güncel hali DB'den alınıp Redis'e yazılacak
        redisTemplate.delete("wallet:" + fromEmail);
        redisTemplate.delete("wallet:" + toEmail);
        // Transfer başarılı olduktan sonra mesaj gönder
        String message = fromEmail + " kullanıcısı " + toEmail + " kullanıcısına " + amount + " TL gönderdi.";
        kafkaProducerService.sendMessage("wallet-transactions", message);
    }
    public List<Wallet> getAllWallets() {
        return walletRepository.findAll(); // Veritabanındaki her şeyi getirir
    }
    @Transactional
    public void deleteWallet(String email) {
        // 1. Önce DB'den sil
        walletRepository.deleteByUserEmail(email);
        // 2. Redis'teki önbelleği temizle
        redisTemplate.delete("wallet:" + email);
        // 3. Kafka'ya haber uçur (Opsiyonel ama yakışıklı olur)
        kafkaProducerService.sendMessage("wallet-transactions", email + " cüzdanı sistemden silindi.");
    }
}