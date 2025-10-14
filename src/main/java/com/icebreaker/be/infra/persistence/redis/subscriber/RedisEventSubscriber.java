package com.icebreaker.be.infra.persistence.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.infra.persistence.redis.handler.MessageHandler;
import com.icebreaker.be.infra.persistence.redis.handler.MessageHandlerRegistry;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisEventSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final MessageHandlerRegistry registry;

    //Redis 채널로 들어온 메시지를 받아서, 최종적으로 사용자에게 보내야함
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String jsonMessage = new String(message.getBody(), StandardCharsets.UTF_8);
            PubSubMessage<?> pubSubMessage = objectMapper.readValue(jsonMessage,
                    PubSubMessage.class);
            log.info("Received redis message: {}", jsonMessage);

            MessageHandler handler = registry.getHandler(pubSubMessage.getType());
            handler.handleAndSend(pubSubMessage.getMessage());

            log.info("Successfully processed message for type: {}", pubSubMessage.getType());
        } catch (IOException e) {
            log.error("Failed to parse Redis message", e);
        }

    }
}
