package com.icebreaker.be.infra.persistence;

import com.icebreaker.be.domain.room.WaitingRoom;
import com.icebreaker.be.domain.room.WaitingRoomRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RedisWaitingRoomRepository implements WaitingRoomRepository {

    private final RedisTemplate<String, WaitingRoom> redisTemplate;

    public RedisWaitingRoomRepository(RedisTemplate<String, WaitingRoom> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(WaitingRoom waitingRoom) {
        redisTemplate.opsForValue().set(waitingRoom.getRoomId(), waitingRoom);
    }

    @Override
    public Optional<WaitingRoom> findById(String roomId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(roomId));
    }

    @Override
    public void delete(String roomId) {
        redisTemplate.delete(roomId);
    }
}
