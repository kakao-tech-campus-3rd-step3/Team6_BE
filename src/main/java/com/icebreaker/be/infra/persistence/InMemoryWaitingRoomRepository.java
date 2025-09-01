package com.icebreaker.be.infra.persistence;

import com.icebreaker.be.domain.room.WaitingRoom;
import com.icebreaker.be.domain.room.WaitingRoomRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryWaitingRoomRepository implements WaitingRoomRepository {

    private final Map<String, WaitingRoom> storage = new ConcurrentHashMap<>();

    @Override
    public void save(WaitingRoom waitingRoom) {
        storage.put(waitingRoom.getRoomId(), waitingRoom);
    }

    @Override
    public Optional<WaitingRoom> findById(String roomId) {
        return Optional.ofNullable(storage.get(roomId));
    }

    @Override
    public void delete(String roomId) {
        storage.remove(roomId);
    }
}
