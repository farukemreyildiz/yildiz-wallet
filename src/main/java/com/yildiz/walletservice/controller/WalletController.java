package com.yildiz.walletservice.controller;

import com.yildiz.walletservice.entity.Wallet;
import com.yildiz.walletservice.service.WalletService;
import org.springframework.web.bind.annotation.*;
import com.yildiz.walletservice.entity.TransferRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * REST API uç noktalarını (endpoints) tanımlayan ve istemciden gelen HTTP isteklerini
 * servis katmanına yönlendiren kontrolcü sınıfıdır.
 */
@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    // Bağımlılıkların (dependency) constructor üzerinden enjekte edilmesi (DI) tercih edilmiştir.
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * Yeni bir cüzdan oluşturur.
     * İstemciden gelen JSON verisini @RequestBody ile Wallet nesnesine eşler.
     */
    @PostMapping("/create")
    public Wallet createWallet(@RequestBody Wallet wallet) {
        return walletService.createWallet(wallet.getUserEmail(), wallet.getBalance());
    }

    /**
     * İki cüzdan arasında para transferi işlemini başlatır.
     * Karmaşık transfer verilerini TransferRequest DTO'su üzerinden kabul eder.
     */
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        walletService.transferMoney(
                request.getFromEmail(),
                request.getToEmail(),
                new java.math.BigDecimal(String.valueOf(request.getAmount()))
        );
        return "Transfer başarıyla tamamlandı!";
    }

    /**
     * Veritabanındaki tüm cüzdan kayıtlarını liste olarak döner.
     */
    @GetMapping("/all")
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }

    /**
     * Belirli bir e-posta adresine ait cüzdanı sistemden kalıcı olarak siler.
     * Silinecek mail adresi URL üzerinden @PathVariable olarak alınır.
     */
    @DeleteMapping("/{email}")
    public String deleteWallet(@PathVariable String email) {
        walletService.deleteWallet(email);
        return "Cüzdan başarıyla silindi.";
    }

    /**
     * Tek bir cüzdanın detaylarını e-posta üzerinden sorgular.
     * Frontend tarafındaki bakiye sorgulama ekranı bu ucu kullanır.
     */
    @GetMapping("/{email}")
    public Wallet getWallet(@PathVariable String email) {
        return walletService.getWalletByEmail(email);
    }
}