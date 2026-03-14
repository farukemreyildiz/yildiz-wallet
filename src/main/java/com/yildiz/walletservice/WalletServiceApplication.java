package com.yildiz.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Uygulamanın ana giriş kapısı.
// @SpringBootApplication ile Spring'in otomatik yapılandırma, bileşen tarama ve
// konfigürasyon süreçlerini bu seviyeden itibaren tetikliyoruz.
@SpringBootApplication
public class WalletServiceApplication {

    public static void main(String[] args) {
        // Uygulamanın runtime sürecini başlatan ana metot.
        // Spring Application Context bu satırla beraber yüklenir ve tüm bean'ler ayağa kalkar.
        SpringApplication.run(WalletServiceApplication.class, args);
    }

}