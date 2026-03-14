package com.yildiz.walletservice.repository;

import com.yildiz.walletservice.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Persistence (Kalıcılık) katmanında Wallet entity'si ile veritabanı arasındaki iletişimi sağlar.
 * JpaRepository genişletilerek temel CRUD (kaydet, sil, listele) fonksiyonları devralınmıştır.
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * E-posta adresine göre cüzdan kaydını getirir.
     * Spring Data JPA "findBy" isimlendirmesi sayesinde SQL sorgusunu otomatik oluşturur.
     * Cüzdanın bulunmama ihtimaline karşı null dönmek yerine Optional konteynerı tercih edilmiştir.
     */
    Optional<Wallet> findByUserEmail(String userEmail);

    /**
     * Belirtilen e-posta adresine sahip cüzdan kaydını sistemden siler.
     * İşlem başarılı olması için WalletService tarafında @Transactional ile sarmalanması gerekir.
     */
    void deleteByUserEmail(String userEmail);
}