package com.icebreaker.be.infra.persistence.redis.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void publish(Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            customStringRedisTemplate.convertAndSend(channelTopic.getTopic(), jsonMessage);
            log.info("Successfully published to topic '{}': {}", channelTopic.getTopic(),
                    jsonMessage);
        } catch (Exception e) {
            log.error("Failed to publish message to Redis", e);
        }
    }
}
