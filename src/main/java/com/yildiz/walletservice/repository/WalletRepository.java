package com.yildiz.walletservice.repository;

import com.yildiz.walletservice.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // Mevcut sorgun muhtemelen buydu:
    Optional<Wallet> findByUserEmail(String userEmail);

    // YENİ EKLEMEN GEREKEN SATIR:
    void deleteByUserEmail(String userEmail);
}