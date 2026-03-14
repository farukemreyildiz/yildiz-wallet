package com.yildiz.walletservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

/**
 * Uygulama genelindeki tüm Controller sınıflarından fırlatılan hataları merkezi bir noktadan
 * yakalamak ve istemciye standart bir hata formatı (ErrorResponse) dönmek için tasarlanmıştır.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * WalletNotFoundException tipindeki hataları yakalar.
     * Veritabanında cüzdan bulunamadığında fırlatılan bu hatayı, HTTP 404 (NOT_FOUND) durum kodu
     * ve hata detaylarını içeren bir nesneyle paketler.
     */
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWalletNotFound(WalletNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), // 404 durum kodu
                ex.getMessage(), // Exception içindeki mesaj
                LocalDateTime.now() // Hatanın oluştuğu anlık zaman bilgisi
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Kodun herhangi bir yerinde fırlatılan ve daha spesifik bir handler tarafından yakalanmayan
     * tüm genel hataları (Exception.class) ele alır.
     * Beklenmedik durumlar için HTTP 500 (INTERNAL_SERVER_ERROR) döner.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500 durum kodu
                "Sistemde beklenmedik bir hata oluştu: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}