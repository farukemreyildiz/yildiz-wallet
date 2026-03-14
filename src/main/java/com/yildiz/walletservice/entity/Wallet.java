package com.yildiz.walletservice.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Veritabanındaki "wallets" tablosu ile bu sınıfı eşleyen ana varlık (entity) sınıfıdır.
 * JPA (Jakarta Persistence API) standartlarına uygun olarak tasarlanmıştır.
 */
@Entity
@Table(name = "wallets")
public class Wallet {

    /**
     * Her cüzdan için eşsiz olan birincil anahtar (Primary Key).
     * IDENTITY stratejisi ile ID üretimi veritabanı seviyesinde (Auto-increment) yönetilir.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cüzdan sahibinin e-posta adresi.
     * Veritabanı seviyesinde "unique" ve "not null" kısıtlamaları (constraint) eklenmiştir.
     */
    @Column(nullable = false, unique = true)
    private String userEmail;

    /**
     * Cüzdan bakiyesi.
     * Kayan noktalı sayı hatalarını önlemek ve finansal hassasiyeti korumak için
     * Double yerine BigDecimal tercih edilmiştir.
     */
    @Column(nullable = false)
    private BigDecimal balance;

    /**
     * Hesabın para birimi (örn: TRY, USD).
     */
    @Column(nullable = false)
    private String currency;

    /**
     * JPA'nın veritabanından veri çekerken (Reflection kullanarak) nesne üretebilmesi için
     * parametresiz (default) constructor zorunludur.
     */
    public Wallet() {
    }

    /**
     * Uygulama içerisinde manuel olarak cüzdan nesnesi oluşturmak için kullanılan
     * yardımcı yapıcı metot.
     */
    public Wallet(Long id, String userEmail, BigDecimal balance, String currency) {
        this.id = id;
        this.userEmail = userEmail;
        this.balance = balance;
        this.currency = currency;
    }

    /** * Veri kapsülleme (Encapsulation) prensibi gereği alanlara erişim
     * bu metotlar üzerinden sağlanır.
     */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}