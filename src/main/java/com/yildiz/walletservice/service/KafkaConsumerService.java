package com.yildiz.walletservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka broker'ından gelen mesajları dinleyen ve işleyen servis katmanıdır.
 * Producer tarafından fırlatılan olayları (events) yakalamak için kullanılır.
 */
@Service
public class KafkaConsumerService {

    /**
     * Belirtilen topic'i (wallet-transactions) sürekli izleyen dinleyici metot.
     * "wallet-group" kimliğiyle Kafka'ya bağlanarak kuyruktaki mesajları tüketir.
     * Mesaj geldiği anda bu metot otomatik olarak tetiklenir.
     */
    @KafkaListener(topics = "wallet-transactions", groupId = "wallet-group")
    public void consume(String message) {
        // Gelen mesajı şimdilik sadece terminale basıyoruz.
        // Gerçek hayat senaryosunda burada log dosyasına yazma veya
        // kullanıcıya push notification gönderme gibi işlemler yapılır.
        System.out.println("KAFKA'DAN MESAJ GELDİ: " + message);
    }
}