package com.icebreaker.be.domain.room;

import java.util.Optional;

public interface WaitingRoomRepository {

    void save(WaitingRoom waitingRoom);

    Optional<WaitingRoom> findById(String roomId);

    void delete(String roomId);
}
