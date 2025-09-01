package com.icebreaker.be.domain.room;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class WaitingRoom {

    private final String roomId;
    private final int capacity;
    private final Set<Long> participants = new HashSet<>();

    private final String name;

    public WaitingRoom(String roomId, String name, int capacity) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
    }

    public synchronized WaitingRoomStatus join(Long userId) {
        participants.add(userId);
        if (!isFull()) {
            return WaitingRoomStatus.WAITING;
        } else {
            return WaitingRoomStatus.FULL;
        }
    }

    public boolean isFull() {
        return participants.size() >= capacity;
    }

    public Set<Long> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }
}
