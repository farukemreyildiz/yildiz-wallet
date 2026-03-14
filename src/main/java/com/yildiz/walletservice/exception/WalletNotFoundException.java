package com.yildiz.walletservice.exception;

/**
 * Veritabanında veya sistemde aranan bir cüzdan kaydı bulunamadığında fırlatılacak özel hata sınıfı.
 * Standart bir hata yerine "Cüzdan Bulunamadı" şeklinde spesifik bir hata tipi tanımlayarak,
 * GlobalExceptionHandler gibi yapılarda bu durumu daha kolay yakalamamızı sağlar.
 */
public class WalletNotFoundException extends RuntimeException {

    /**
     * RuntimeException'dan türetilmesinin sebebi, bu hatayı fırlatırken metodun imzasında
     * 'throws' anahtar kelimesiyle belirtme zorunluluğunu (unchecked exception) ortadan kaldırmaktır.
     * Bu sayede kod akışını daha temiz bir şekilde yönetebiliyoruz.
     */
    public WalletNotFoundException(String message) {
        // Gelen spesifik hata mesajını (örneğin: "Cüzdan bulunamadı: test@mail.com")
        // üst sınıf olan RuntimeException'a paslar.
        super(message);
    }
}