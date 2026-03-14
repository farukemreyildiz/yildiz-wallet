package com.yildiz.walletservice.exception;

import java.time.LocalDateTime;

/**
 * API katmanında bir hata oluştuğunda, istemciye (frontend) dönecek olan
 * hata mesajının standart yapısını belirler.
 */
public class ErrorResponse {
    // HTTP durum kodu (404, 500, 400 gibi teknik karşılıklar).
    private int status;

    // Hatanın ne olduğunu açıklayan okunabilir metin.
    private String message;

    // Hatanın tam olarak hangi saniyede gerçekleştiğini tutan zaman damgası.
    private LocalDateTime timestamp;

    /**
     * Hata oluştuğu anda GlobalExceptionHandler tarafında
     * hızlıca nesne üretmek için kullanılan yapıcı metot.
     */
    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    /** * Verilerin JSON formatına serileştirilip dışarı aktarılabilmesi için
     * gerekli olan erişim (Getter) metotları.
     */
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}