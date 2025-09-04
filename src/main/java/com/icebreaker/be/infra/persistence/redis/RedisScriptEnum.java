package com.icebreaker.be.infra.persistence.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.script.RedisScript;

@Getter
@RequiredArgsConstructor
public enum RedisScriptEnum {
    CREATE_ROOM("lua/create_room.lua"),
    JOIN_ROOM("lua/join_room.lua");

    private final String classpath;
    private RedisScript<String> redisScript;

    public void addRedisScript(RedisScript<String> redisScript) {
        this.redisScript = redisScript;
    }
}

