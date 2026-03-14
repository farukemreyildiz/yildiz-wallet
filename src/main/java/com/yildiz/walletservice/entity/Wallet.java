package com.yildiz.walletservice.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currency;

    // 1. Boş Constructor (JPA için zorunludur)
    public Wallet() {
    }

    // 2. Dolu Constructor (Elle nesne oluştururken lazım olur)
    public Wallet(Long id, String userEmail, BigDecimal balance, String currency) {
        this.id = id;
        this.userEmail = userEmail;
        this.balance = balance;
        this.currency = currency;
    }

    // 3. Getter ve Setter Metotları
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}