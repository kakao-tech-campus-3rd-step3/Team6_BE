package com.icebreaker.be.infra;

import com.icebreaker.be.domain.room.WaitingRoom;
import com.icebreaker.be.domain.room.WaitingRoomRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

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
