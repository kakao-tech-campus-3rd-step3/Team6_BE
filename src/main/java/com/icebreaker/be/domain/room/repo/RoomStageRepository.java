package com.icebreaker.be.domain.room.repo;

import com.icebreaker.be.domain.room.entity.RoomStage;
import java.util.Optional;

public interface RoomStageRepository {

    void save(RoomStage stage);

    Optional<RoomStage> findByRoomCode(String roomCode);

    void delete(String roomCode);
}
