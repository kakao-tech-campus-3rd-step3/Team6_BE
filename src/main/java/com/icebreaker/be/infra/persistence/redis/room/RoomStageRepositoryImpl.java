package com.icebreaker.be.infra.persistence.redis.room;

import com.icebreaker.be.domain.room.entity.RoomStage;
import com.icebreaker.be.domain.room.entity.Stage;
import com.icebreaker.be.domain.room.repo.RoomStageRepository;
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
    public void save(RoomStage stage) {
        String key = getStageKey(stage.roomCode());
        customStringRedisTemplate.opsForValue().set(key, stage.currentStage().name());
        log.debug("Saved stage {} for room {}", stage.currentStage(), stage.roomCode());
    }

    @Override
    public Optional<RoomStage> findByRoomCode(String roomCode) {
        String key = getStageKey(roomCode);
        String stageStr = customStringRedisTemplate.opsForValue().get(key);
        if (stageStr == null) {
            return Optional.empty();
        }

        RoomStage stage = new RoomStage(roomCode, Stage.valueOf(stageStr));
        log.debug("Found stage {} for room {}", stage.currentStage(), roomCode);
        return Optional.of(stage);
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
