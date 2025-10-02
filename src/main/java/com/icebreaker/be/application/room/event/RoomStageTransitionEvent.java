package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;
import com.icebreaker.be.domain.room.vo.StageTransitionEvent;

public record RoomStageTransitionEvent(
        String roomCode,
        StageTransitionEvent stageEvent
) {

    public static RoomStageTransitionEvent init(String roomCode) {
        return new RoomStageTransitionEvent(roomCode, StageTransitionEvent.init());
    }

    public static RoomStageTransitionEvent next(String roomCode) {
        return new RoomStageTransitionEvent(roomCode, StageTransitionEvent.next());
    }

    public static RoomStageTransitionEvent prev(String roomCode) {
        return new RoomStageTransitionEvent(roomCode, StageTransitionEvent.prev());
    }

    public static RoomStageTransitionEvent select(String roomCode, Stage stage) {
        return new RoomStageTransitionEvent(roomCode, StageTransitionEvent.select(stage));
    }

    public StageEventType stageEventType() {
        return stageEvent.type();
    }

    public Stage targetStage() {
        return stageEvent.target();
    }
}
