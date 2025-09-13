package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.entity.Stage;

public record RoomStageChangeEvent(
        String roomCode,
        Stage stage
) {

    public static RoomStageChangeEvent of(String roomCode, Stage stage) {
        return new RoomStageChangeEvent(roomCode, stage);
    }
}
