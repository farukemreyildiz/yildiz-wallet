package com.yildiz.walletservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key'ler her zaman düz metin (String) olarak saklanır
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // MODERN YÖNTEM: Spring 4.0+ için en temiz yol
        // Bu metot arka planda ObjectMapper'ı ve JavaTimeModule'ü otomatik yönetir.
        // Manuel constructor (yapıcı) çağırmadığımız için "Deprecated" hatası almazsın.
        RedisSerializer<Object> serializer = RedisSerializer.json();

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }
}