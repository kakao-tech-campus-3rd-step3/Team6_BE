package com.icebreaker.be.domain.room.vo;

public record RoomStage(String roomCode, Stage stage) {

    public static RoomStage of(String roomCode, Stage stage) {
        return new RoomStage(roomCode, stage);
    }
}
