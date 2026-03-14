package com.yildiz.walletservice.entity; // Daha sonra 'dto' paketine taşımanı öneririm kanka

import java.math.BigDecimal;

/**
 * Para transferi işlemleri sırasında istemciden (frontend) gelen JSON verisini
 * Java nesnesine dönüştürmek için kullanılan yardımcı sınıftır.
 */
public class TransferRequest {
    // Parayı gönderecek olan cüzdanın e-posta adresi.
    private String fromEmail;

    // Paranın ulaşacağı hedef cüzdanın e-posta adresi.
    private String toEmail;

    // Transfer edilmek istenen tutar. Hassasiyet kaybı yaşanmaması için BigDecimal tipindedir.
    private BigDecimal amount;

    /**
     * Spring'in Jackson kütüphanesi aracılığıyla JSON verisini bu sınıfa
     * eşleyebilmesi (Deserialization) için parametresiz yapıcı metot zorunludur.
     */
    public TransferRequest() {
    }

    /** * Alanlara kontrollü erişim ve veri ataması sağlayan standart Getter ve Setter metotları.
     */
    public String getFromEmail() { return fromEmail; }
    public void setFromEmail(String fromEmail) { this.fromEmail = fromEmail; }

    public String getToEmail() { return toEmail; }
    public void setToEmail(String toEmail) { this.toEmail = toEmail; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}