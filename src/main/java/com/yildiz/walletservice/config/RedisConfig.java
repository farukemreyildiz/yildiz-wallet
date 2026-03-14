package com.yildiz.walletservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Uygulamanın Redis ile veri alışverişi yaparken kullanacağı şablonları (Template)
 * ve verinin nasıl serileştirileceğini (Serialization) belirleyen yapılandırma sınıfıdır.
 */
@Configuration
public class RedisConfig {

    /**
     * Redis üzerinde işlem yapmamızı sağlayan ana aracı (RedisTemplate) oluşturur.
     * Verilerin Redis-cli üzerinden okunabilir olması için özelleştirilmiştir.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Redis anahtarlarını (Keys) düz metin formatında saklamak için StringRedisSerializer kullanılır.
        // Bu sayede terminalden bakıldığında anahtarlar karmaşık (binary) değil, okunabilir görünür.
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Değerlerin (Values) saklanması için modern JSON serileştirme yöntemi tercih edilmiştir.
        // Bu yöntem Java nesnelerini JSON formatına dönüştürür ve zaman damgaları gibi karmaşık
        // veri tiplerini otomatik olarak yönetir.
        RedisSerializer<Object> serializer = RedisSerializer.json();

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }
}