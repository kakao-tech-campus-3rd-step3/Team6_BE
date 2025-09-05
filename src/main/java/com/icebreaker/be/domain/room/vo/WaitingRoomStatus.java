package com.icebreaker.be.domain.room.vo;

public enum WaitingRoomStatus {
    AVAILABLE,
    FULL;

    public boolean isFull() {
        return this == FULL;
    }
}
