package com.yildiz.walletservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Uygulama içerisindeki önemli olayları (events) Kafka broker'ına iletmekten sorumlu servis sınıfıdır.
 * Sistemler arası asenkron iletişimi sağlamak için kullanılır.
 */
@Service
public class KafkaProducerService {

    // Kafka ile mesaj alışverişini kolaylaştıran Spring kütüphanesi şablonu.
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Belirtilen topic'e (konu başlığına) string formatında bir mesaj gönderir.
     * Bu işlem asenkron çalışır; yani mesajın iletilmesini beklemeden ana iş akışı (thread) yoluna devam eder.
     */
    public void sendMessage(String topic, String message) {
        // Mesajı Kafka broker'ındaki ilgili bölüme (partition) sevk eder.
        kafkaTemplate.send(topic, message);
    }
}