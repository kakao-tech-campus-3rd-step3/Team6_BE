package com.icebreaker.be.infra.persistence.redis.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.infra.persistence.redis.message.RedisMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, String> customStringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final ChannelTopic channelTopic;

    public void publish(RedisMessage<?> message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            customStringRedisTemplate.convertAndSend(channelTopic.getTopic(), jsonMessage);
            log.info("Successfully published to topic '{}': {}", channelTopic.getTopic(),
                    jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to publish message to Redis", e);
        }
    }
}
