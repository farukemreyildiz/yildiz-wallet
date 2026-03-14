package com.yildiz.walletservice.exception;

// Kendi özel hata sınıfımız
public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}