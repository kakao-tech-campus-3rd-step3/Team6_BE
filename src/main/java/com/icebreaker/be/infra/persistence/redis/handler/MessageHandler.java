package com.icebreaker.be.infra.persistence.redis.handler;

import com.icebreaker.be.infra.persistence.redis.message.PubSubMessageType;

public interface MessageHandler {

    PubSubMessageType getMessageType();

    void handleAndSend(Object payload);
}
