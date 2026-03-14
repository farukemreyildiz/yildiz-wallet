package com.yildiz.walletservice.controller;

import com.yildiz.walletservice.entity.Wallet;
import com.yildiz.walletservice.service.WalletService;
import org.springframework.web.bind.annotation.*;
import com.yildiz.walletservice.entity.TransferRequest; // Sınıfı buraya tanıtıyoruz
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // Cüzdan Oluşturma: Artık JSON bekliyor
    @PostMapping("/create")
    public Wallet createWallet(@RequestBody Wallet wallet) {
        // Wallet nesnesinin içindeki verileri kullanıyoruz
        return walletService.createWallet(wallet.getUserEmail(), wallet.getBalance());
    }

    // Transfer İşlemi: Artık TransferRequest JSON'u bekliyor
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        walletService.transferMoney(
                request.getFromEmail(),
                request.getToEmail(),
                new java.math.BigDecimal(String.valueOf(request.getAmount()))
        );
        return "Transfer başarıyla tamamlandı!";
    }
    @GetMapping("/all")
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }
    @DeleteMapping("/{email}")
    public String deleteWallet(@PathVariable String email) {
        walletService.deleteWallet(email);
        return "Cüzdan başarıyla silindi.";
    }
    // YENİ EKLEDİĞİMİZ KISIM: Cüzdan sorgulama kapısı
    @GetMapping("/{email}")
    public Wallet getWallet(@PathVariable String email) {
        return walletService.getWalletByEmail(email);
    }
}