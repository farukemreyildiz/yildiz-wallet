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

/**
 * İş mantığının (Business Logic) yürütüldüğü ana servis sınıfıdır.
 * Veritabanı (PostgreSQL), Önbellek (Redis) ve Mesaj Kuyruğu (Kafka) koordinasyonunu sağlar.
 */
@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final StringRedisTemplate redisTemplate;
    private final KafkaProducerService kafkaProducerService;

    // Bağımlılıklar Constructor üzerinden enjekte edilerek test edilebilir bir yapı kurulmuştur.
    public WalletService(WalletRepository walletRepository,
                         StringRedisTemplate redisTemplate,
                         KafkaProducerService kafkaProducerService) {
        this.walletRepository = walletRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Yeni bir cüzdan oluşturur ve ilk bakiyeyi hem DB'ye yazar hem de Redis'te önbelleğe alır.
     */
    public Wallet createWallet(String email, BigDecimal initialBalance) {
        Wallet wallet = new Wallet();
        wallet.setUserEmail(email);
        wallet.setBalance(initialBalance);
        wallet.setCurrency("TRY");
        Wallet savedWallet = walletRepository.save(wallet);

        // Sorgu hızını artırmak için bakiye bilgisini 30 dakikalık bir süreyle Redis'e atıyoruz.
        redisTemplate.opsForValue().set("wallet:" + email, initialBalance.toString(), Duration.ofMinutes(30));

        return savedWallet;
    }

    /**
     * "Cache-Aside" stratejisi ile cüzdan bilgisini getirir.
     * Önce hızlı katman olan Redis'e bakılır, veri yoksa (Cache Miss) veritabanına gidilir.
     */
    public Wallet getWalletByEmail(String email) {
        // 1. Önbellek kontrolü
        String cachedBalance = redisTemplate.opsForValue().get("wallet:" + email);

        if (cachedBalance != null) {
            System.out.println("DEBUG: Bakiye Redis'ten getirildi!");
            Wallet wallet = new Wallet();
            wallet.setUserEmail(email);
            wallet.setBalance(new BigDecimal(cachedBalance));
            wallet.setCurrency("TRY");
            return wallet;
        }

        // 2. Cache Miss: Veritabanından çekme ve hata yönetimi
        System.out.println("DEBUG: Bakiye DB'den getirildi!");
        Wallet wallet = walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new WalletNotFoundException("Cüzdan bulunamadı!"));

        // 3. Bir sonraki istek için Redis'i güncelleme
        redisTemplate.opsForValue().set("wallet:" + email, wallet.getBalance().toString(), Duration.ofMinutes(30));

        return wallet;
    }

    /**
     * İki hesap arasındaki para transferini yönetir.
     * Veri bütünlüğü için @Transactional ile işaretlenmiştir; işlem sırasında oluşacak bir hata
     * tüm sürecin geri alınmasını (Rollback) sağlar.
     */
    @Transactional
    public void transferMoney(String fromEmail, String toEmail, BigDecimal amount) {
        Wallet fromWallet = walletRepository.findByUserEmail(fromEmail)
                .orElseThrow(() -> new WalletNotFoundException("Gönderen cüzdan bulunamadı: " + fromEmail));

        Wallet toWallet = walletRepository.findByUserEmail(toEmail)
                .orElseThrow(() -> new WalletNotFoundException("Alıcı cüzdan bulunamadı: " + toEmail));

        // İşlem öncesi iş kuralı (Business Rule) kontrolü.
        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Yetersiz bakiye!");
        }

        // Bakiye güncellemeleri
        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        // "Cache Invalidation": Bakiyeler değiştiği için Redis'teki eski veriler temizlenir.
        redisTemplate.delete("wallet:" + fromEmail);
        redisTemplate.delete("wallet:" + toEmail);

        // İşlem logu asenkron olarak Kafka üzerinden fırlatılır.
        String message = fromEmail + " kullanıcısı " + toEmail + " kullanıcısına " + amount + " TL gönderdi.";
        kafkaProducerService.sendMessage("wallet-transactions", message);
    }

    /**
     * Tüm cüzdanları listeler; genelde yönetim paneli için kullanılır.
     */
    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    /**
     * Cüzdanı sistemden tamamen siler.
     * DB'den silme işlemi sonrası önbellek temizliği ve Kafka bildirimi yapılır.
     */
    @Transactional
    public void deleteWallet(String email) {
        walletRepository.deleteByUserEmail(email);
        redisTemplate.delete("wallet:" + email);
        kafkaProducerService.sendMessage("wallet-transactions", email + " cüzdanı sistemden silindi.");
    }
}