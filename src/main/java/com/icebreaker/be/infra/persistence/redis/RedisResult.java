package com.icebreaker.be.infra.persistence.redis;

public record RedisResult<S, T>(
        S status,
        T data
) {

}
