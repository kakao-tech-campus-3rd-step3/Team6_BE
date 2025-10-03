package com.icebreaker.be.infra.persistence.redis.room;

import com.icebreaker.be.domain.room.repo.RoomOwnerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoomOwnerRepositoryImpl implements RoomOwnerRepository {

    private static final String PREFIX = "room:";
    private static final String OWNER_SUFFIX = ":owner";

    private final RedisTemplate<String, String> customStringRedisTemplate;

    @Override
    public void save(String roomCode, Long ownerId) {
        String key = getOwnerKey(roomCode);
        customStringRedisTemplate.opsForValue().set(key, ownerId.toString());
        log.debug("Saved owner {} for room {}", ownerId, roomCode);
    }

    @Override
    public Optional<Long> findOwnerByRoomCode(String roomCode) {
        String key = getOwnerKey(roomCode);
        String ownerId = customStringRedisTemplate.opsForValue().get(key);
        if (ownerId == null) {
            return Optional.empty();
        }
        log.debug("Found owner {} for room {}", ownerId, roomCode);
        return Optional.of(Long.valueOf(ownerId));
    }

    @Override
    public void delete(String roomCode) {
        String key = getOwnerKey(roomCode);
        customStringRedisTemplate.delete(key);
        log.debug("Deleted owner for room {}", roomCode);
    }

    private String getOwnerKey(String roomCode) {
        return PREFIX + roomCode + OWNER_SUFFIX;
    }
}

