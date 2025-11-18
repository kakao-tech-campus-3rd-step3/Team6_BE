package com.icebreaker.be.global.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private static final String REDIS_PROTOCOL = "redis://";

    private final RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        String address = String.format("%s%s:%d", REDIS_PROTOCOL,
                redisProperties.getHost(),
                redisProperties.getPort());

        Config config = new Config();
        config.useSingleServer().setAddress(address);
        return Redisson.create(config);
    }
}