package com.icebreaker.be.infra.persistence.redis;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
public class RedisScriptRegistry {

    private final Map<RedisScriptEnum, RedisScript<String>> scriptMap = new EnumMap<>(
            RedisScriptEnum.class);
    
    @PostConstruct
    public void init() {
        Arrays.stream(RedisScriptEnum.values())
                .forEach(e -> {
                    RedisScript<String> script = loadScriptOrThrow(e.getClasspath(), e);
                    scriptMap.put(e, script);
                    e.addRedisScript(script);
                });
    }

    public RedisScript<String> get(RedisScriptEnum scriptEnum) {
        RedisScript<String> script = scriptMap.get(scriptEnum);
        if (script == null) {
            throw new IllegalArgumentException("No Redis script found for: " + scriptEnum.name());
        }
        return script;
    }

    private RedisScript<String> loadScriptOrThrow(String classpath, RedisScriptEnum e) {
        try {
            return RedisScript.of(new ClassPathResource(classpath), String.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load Redis script: " + e.name(), ex);
        }
    }
}