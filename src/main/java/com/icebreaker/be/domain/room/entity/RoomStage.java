package com.icebreaker.be.domain.room.entity;

public record RoomStage(String roomCode, Stage currentStage) {

    public static RoomStage init(String roomCode) {
        return new RoomStage(roomCode, Stage.STAGE_1);
    }

    public static RoomStage of(String roomCode, Stage stage) {
        return new RoomStage(roomCode, stage);
    }
}
