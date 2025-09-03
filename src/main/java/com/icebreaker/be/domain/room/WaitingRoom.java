package com.icebreaker.be.domain.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@RedisHash(value = "waitingRoom", timeToLive = 3600)
public class WaitingRoom implements Serializable {

    @Id
    private String roomId;

    private String name;

    private int capacity;

    private Set<Long> participants = new HashSet<>();

    public WaitingRoom(String roomId, String name, int capacity) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.participants = new HashSet<>();
    }

    public synchronized WaitingRoomStatus join(Long userId) {
        participants.add(userId);
        return isFull() ? WaitingRoomStatus.FULL : WaitingRoomStatus.WAITING;
    }

    public boolean isFull() {
        return participants.size() >= capacity;
    }

    public Set<Long> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }
}