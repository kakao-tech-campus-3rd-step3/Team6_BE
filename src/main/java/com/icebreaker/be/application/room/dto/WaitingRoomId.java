package com.icebreaker.be.application.room.dto;

public record WaitingRoomId(
        String roomId
) {

    public static WaitingRoomId of(String roomId) {
        return new WaitingRoomId(roomId);
    }
}
