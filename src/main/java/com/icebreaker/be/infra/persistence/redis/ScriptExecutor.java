package com.icebreaker.be.infra.persistence.redis;

import java.util.List;


public interface ScriptExecutor {

    <R, S> R execute(
            RedisScriptEnum scriptEnum,
            List<String> keys,
            RedisArgs args,
            Class<R> resultClass,
            StatusMapper<S> statusMapper
    );
}