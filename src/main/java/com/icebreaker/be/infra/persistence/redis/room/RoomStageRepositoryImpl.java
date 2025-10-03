package com.icebreaker.be.infra.persistence.redis.room;

import com.icebreaker.be.domain.room.repo.RoomStageRepository;
import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoomStageRepositoryImpl implements RoomStageRepository {

    private static final String PREFIX = "room:";
    private static final String STAGE_SUFFIX = ":stage";

    private final RedisTemplate<String, String> customStringRedisTemplate;

    @Override
    public void save(RoomStage roomStage) {
        String key = getStageKey(roomStage.roomCode());
        customStringRedisTemplate.opsForValue().set(key, roomStage.stage().name());
        log.debug("Saved stage {} for room {}", roomStage.stage(), roomStage.roomCode());
    }

    @Override
    public Optional<RoomStage> findByRoomCode(String roomCode) {
        String key = getStageKey(roomCode);
        String stageStr = customStringRedisTemplate.opsForValue().get(key);
        if (stageStr == null) {
            return Optional.empty();
        }
        try {
            RoomStage roomStage = new RoomStage(roomCode, Stage.valueOf(stageStr));
            log.debug("Found stage {} for room {}", roomStage.stage(), roomCode);
            return Optional.of(roomStage);
        } catch (IllegalArgumentException e) {
            log.error("Invalid stage value '{}' found in Redis for roomCode '{}'", stageStr,
                    roomCode, e);
            return Optional.empty();
        }
    }

    @Override
    public void delete(String roomCode) {
        String key = getStageKey(roomCode);
        customStringRedisTemplate.delete(key);
        log.debug("Deleted stage for room {}", roomCode);
    }

    private String getStageKey(String roomCode) {
        return PREFIX + roomCode + STAGE_SUFFIX;
    }
}
