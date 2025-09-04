package com.icebreaker.be.infra.persistence.redis;

import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;

@Getter
public enum RedisScriptEnum {

    CREATE_ROOM("lua/create_room.lua"),
    JOIN_ROOM("lua/join_room.lua");

    private final String classpath;
    private final RedisScript<String> redisScript;

    RedisScriptEnum(String classpath) {
        this.classpath = classpath;
        this.redisScript = createRedisScript(classpath);
    }

    private static RedisScript<String> createRedisScript(String path) {
        try {
            return RedisScript.of(new ClassPathResource(path), String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Redis script: " + path, e);
        }
    }
}