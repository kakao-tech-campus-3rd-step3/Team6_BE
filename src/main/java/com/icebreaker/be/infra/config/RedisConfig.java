package com.icebreaker.be.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.domain.room.WaitingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, WaitingRoom> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, WaitingRoom> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<WaitingRoom> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, WaitingRoom.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}

