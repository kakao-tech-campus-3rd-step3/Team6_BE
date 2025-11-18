package com.icebreaker.be.infra.persistence.redis.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.domain.topic.TopicRepository;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepository {

    private static final Duration TTL = Duration.ofHours(4);
    private static final String INIT_FIELD = "_initialized";
    private static final String ROOM_PREFIX = "room:";
    private static final String TOPIC_SUFFIX = ":topics";
    private static final String USER_PREFIX = "user:";

    private final RedisTemplate<String, String> customStringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void save(String roomCode, Long userId, List<Topic> topics) {
        if (topics == null || topics.isEmpty()) {
            return;
        }

        String redisKey = getRoomKey(roomCode);
        String userField = getUserField(userId);

        try {
            String json = objectMapper.writeValueAsString(topics);

            customStringRedisTemplate.execute(new SessionCallback<Void>() {

                @Override
                @SuppressWarnings("unchecked")
                public Void execute(@NotNull RedisOperations operations) {
                    var hashOps = operations.opsForHash();
                    Object initialized = hashOps.get(redisKey, INIT_FIELD);
                    operations.multi();
                    if (initialized == null) {
                        hashOps.put(redisKey, INIT_FIELD, "true");
                    }
                    hashOps.put(redisKey, userField, json);
                    operations.expire(redisKey, TTL);
                    operations.exec();
                    return null;
                }
            });

        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize topics", e);
        }
    }


    @Override
    public List<Topic> findByRoomAndUser(String roomCode, Long userId) {
        String redisKey = getRoomKey(roomCode);
        String userField = getUserField(userId);

        Object json = customStringRedisTemplate.opsForHash().get(redisKey, userField);
        if (json == null) {
            return List.of();
        }

        try {
            return Arrays.asList(objectMapper.readValue(json.toString(), Topic[].class));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize topics", e);
        }
    }

    @Override
    public List<Topic> findAllByRoom(String roomCode) {
        String redisKey = getRoomKey(roomCode);
        Map<Object, Object> entries = customStringRedisTemplate.opsForHash().entries(redisKey);

        if (entries.isEmpty()) {
            return List.of();
        }

        return entries.entrySet()
                .stream()
                .filter(entry -> !INIT_FIELD.equals(entry.getKey()))
                .flatMap(entry -> {
                    try {
                        Topic[] topics = objectMapper.readValue(entry.getValue().toString(),
                                Topic[].class);
                        return Arrays.stream(topics);
                    } catch (JsonProcessingException e) {
                        throw new IllegalStateException("Failed to deserialize topics", e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean isInitialized(String roomCode) {
        String redisKey = getRoomKey(roomCode);
        Object flag = customStringRedisTemplate.opsForHash().get(redisKey, INIT_FIELD);
        return flag != null && Boolean.parseBoolean(flag.toString());
    }

    private String getRoomKey(String roomCode) {
        return ROOM_PREFIX + roomCode + TOPIC_SUFFIX;
    }

    private String getUserField(Long userId) {
        return USER_PREFIX + userId;
    }
}
