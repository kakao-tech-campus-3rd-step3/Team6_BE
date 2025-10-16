package com.icebreaker.be.infra.persistence.redis.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PubSubMessage<T extends MessagePayload> {

    private PubSubMessageType type;
    private T message;
}
