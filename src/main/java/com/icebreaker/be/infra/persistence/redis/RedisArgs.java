package com.icebreaker.be.infra.persistence.redis;

/**
 * 안정적인 형태로 Redis 인자를 받기 위해서 공통화된 Interface 정의
 */
public interface RedisArgs {

    Object[] toArray();
}
