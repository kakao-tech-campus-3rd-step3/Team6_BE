package com.icebreaker.be.infra.persistence.redis.handler;

import com.icebreaker.be.infra.persistence.redis.message.PubSubMessageType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerRegistry {

    private final Map<PubSubMessageType, MessageHandler> handlerMap = new HashMap<>();

    public MessageHandlerRegistry(List<MessageHandler> handlers) {
        for (MessageHandler handler : handlers) {
            handlerMap.put(handler.getMessageType(), handler);
        }
    }

    public MessageHandler getHandler(PubSubMessageType messageType) {
        MessageHandler handler = handlerMap.get(messageType);
        if (handler == null) {
            throw new IllegalStateException("적절한 메시지핸들러가 없습니다.");
        }
        return handler;
    }
}
