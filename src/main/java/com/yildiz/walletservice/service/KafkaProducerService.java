package com.yildiz.walletservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        // Bu metod mesajı Kafka'ya fırlatır ve yoluna devam eder (Asenkron)
        kafkaTemplate.send(topic, message);
    }
}