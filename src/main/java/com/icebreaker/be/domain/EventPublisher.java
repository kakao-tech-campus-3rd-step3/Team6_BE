package com.icebreaker.be.domain;

import com.icebreaker.be.infra.persistence.redis.message.RedisMessage;

public interface EventPublisher {

    void publish(RedisMessage<?> message);
}
