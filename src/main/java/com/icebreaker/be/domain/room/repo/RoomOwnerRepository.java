package com.icebreaker.be.domain.room.repo;

import java.util.Optional;

public interface RoomOwnerRepository {

    void save(String roomCode, Long ownerId);

    Optional<Long> findOwnerByRoomCode(String roomCode);

    void delete(String roomCode);
}
