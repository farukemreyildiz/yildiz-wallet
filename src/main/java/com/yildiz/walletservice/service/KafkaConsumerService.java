package com.yildiz.walletservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "wallet-transactions", groupId = "wallet-group")
    public void consume(String message) {
        System.out.println("KAFKA'DAN MESAJ GELDİ: " + message);
    }
}