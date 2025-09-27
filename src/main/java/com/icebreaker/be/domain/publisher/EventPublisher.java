package com.icebreaker.be.domain.publisher;

import com.icebreaker.be.infra.persistence.redis.message.PubSubMessage;

public interface EventPublisher {

    void publish(PubSubMessage<?> message);
}
