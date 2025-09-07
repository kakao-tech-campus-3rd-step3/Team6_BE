package com.icebreaker.be.infra.persistence.redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisScriptExecutor implements ScriptExecutor {

    private final RedisScriptRegistry registry;
    private final RedisTemplate<String, String> customStringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <R, S> R execute(
            RedisScriptEnum scriptEnum,
            List<String> keys,
            RedisArgs args,
            Class<R> resultClass,
            StatusMapper<S> statusMapper
    ) {
        try {
            RedisScript<String> script = registry.get(scriptEnum);
            String resultStr = customStringRedisTemplate.execute(script, keys, args.toArray());

            RedisResult<S, R> result = objectMapper.readValue(
                    resultStr,
                    objectMapper.getTypeFactory()
                            .constructParametricType(RedisResult.class,
                                    statusMapper.getStatusClass(),
                                    resultClass)
            );

            if (!statusMapper.isSuccess(result.status())) {
                throw new BusinessException(statusMapper.toErrorCode(result.status()));
            }

            return result.data();

        } catch (JsonProcessingException ex) {
            log.error("Failed to parse Redis script result for {}", scriptEnum.name(), ex);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Redis script execution failed for {}", scriptEnum.name(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
