package com.icebreaker.be.infra.persistence.redis.handler;

import com.icebreaker.be.infra.persistence.redis.message.MessagePayload;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessageType;

public interface MessageHandler {

    PubSubMessageType getMessageType();

    void handleAndSend(MessagePayload payload);
}
