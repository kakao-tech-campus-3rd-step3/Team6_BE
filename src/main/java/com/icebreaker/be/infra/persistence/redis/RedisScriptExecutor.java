package com.icebreaker.be.infra.persistence.redis;


import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisScriptExecutor {

    private final RedisScriptRegistry registry;
    private final RedisTemplate<String, String> customStringRedisTemplate;

    public String execute(
            RedisScriptEnum scriptEnum,
            List<String> keys,
            RedisArgs args
    ) {
        try {
            RedisScript<String> script = registry.get(scriptEnum);
            return customStringRedisTemplate.execute(script, keys, args.toArray());
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Redis script execution failed for " + scriptEnum.name(), e);
        }
    }
}
