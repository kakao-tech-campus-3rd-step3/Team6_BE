package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;
import jakarta.annotation.Nullable;

public record RoomStageEvent(
        StageEventType type,
        String roomCode,
        @Nullable Stage target
) {

    public static RoomStageEvent init(String roomCode) {
        return new RoomStageEvent(StageEventType.INIT, roomCode, null);
    }

    public static RoomStageEvent next(String roomCode) {
        return new RoomStageEvent(StageEventType.NEXT, roomCode, null);
    }

    public static RoomStageEvent prev(String roomCode) {
        return new RoomStageEvent(StageEventType.PREV, roomCode, null);
    }

    public static RoomStageEvent select(String roomCode, Stage stage) {
        return new RoomStageEvent(StageEventType.SELECT, roomCode, stage);
    }
}
