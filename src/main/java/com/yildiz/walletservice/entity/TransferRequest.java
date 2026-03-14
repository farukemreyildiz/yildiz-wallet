package com.yildiz.walletservice.entity; // Daha sonra 'dto' paketine taşımanı öneririm kanka

import java.math.BigDecimal;

public class TransferRequest {
    private String fromEmail;
    private String toEmail;
    private BigDecimal amount;

    // Boş Constructor (JSON eşleşmesi için şart)
    public TransferRequest() {
    }

    // Getter ve Setter Metotları
    public String getFromEmail() { return fromEmail; }
    public void setFromEmail(String fromEmail) { this.fromEmail = fromEmail; }

    public String getToEmail() { return toEmail; }
    public void setToEmail(String toEmail) { this.toEmail = toEmail; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}