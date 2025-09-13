package com.icebreaker.be.domain.waitingroom;

public enum WaitingRoomStatus {
    AVAILABLE,
    FULL;

    public boolean isFull() {
        return this == FULL;
    }
}
