package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;
import jakarta.annotation.Nullable;

public record RoomStageTransitionEvent(
        StageEventType type,
        String roomCode,
        @Nullable Stage target
) {

    public static RoomStageTransitionEvent init(String roomCode) {
        return new RoomStageTransitionEvent(StageEventType.INIT, roomCode, null);
    }

    public static RoomStageTransitionEvent next(String roomCode) {
        return new RoomStageTransitionEvent(StageEventType.NEXT, roomCode, null);
    }

    public static RoomStageTransitionEvent prev(String roomCode) {
        return new RoomStageTransitionEvent(StageEventType.PREV, roomCode, null);
    }

    public static RoomStageTransitionEvent select(String roomCode, Stage stage) {
        return new RoomStageTransitionEvent(StageEventType.SELECT, roomCode, stage);
    }
}
