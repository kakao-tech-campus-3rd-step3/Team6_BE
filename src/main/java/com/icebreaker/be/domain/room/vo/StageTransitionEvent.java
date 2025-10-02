package com.icebreaker.be.domain.room.vo;

public record StageTransitionEvent(StageEventType type, Stage target) {

    public static StageTransitionEvent init() {
        return new StageTransitionEvent(StageEventType.INIT, null);
    }

    public static StageTransitionEvent next() {
        return new StageTransitionEvent(StageEventType.NEXT, null);
    }

    public static StageTransitionEvent prev() {
        return new StageTransitionEvent(StageEventType.PREV, null);
    }

    public static StageTransitionEvent select(Stage target) {
        return new StageTransitionEvent(StageEventType.SELECT, target);
    }
}
