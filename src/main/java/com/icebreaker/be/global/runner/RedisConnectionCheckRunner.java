package com.icebreaker.be.global.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisConnectionCheckRunner implements CommandLineRunner {
    private final RedisConnectionFactory redisConnectionFactory;


    @Override
    public void run(String... args) throws Exception {
        try {
            redisConnectionFactory.getConnection().ping();
            log.info("✅ Redis 연결에 성공했습니다.");
        } catch (Exception e) {
            log.error("❌ Redis 연결에 실패했습니다: {}", e.getMessage());
            throw e;
        }
    }
}
