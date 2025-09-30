package com.icebreaker.be.domain.room.vo;

public record StageEvent(StageEventType type, Stage target) {

    public static StageEvent init() {
        return new StageEvent(StageEventType.INIT, null);
    }

    public static StageEvent next() {
        return new StageEvent(StageEventType.NEXT, null);
    }

    public static StageEvent prev() {
        return new StageEvent(StageEventType.PREV, null);
    }

    public static StageEvent select(Stage target) {
        return new StageEvent(StageEventType.SELECT, target);
    }

    public static StageEvent selectTopic() {
        return new StageEvent(StageEventType.SELECT, Stage.TOPIC_RECOMMEND_STAGE);
    }

    public static StageEvent selectRandom() {
        return new StageEvent(StageEventType.SELECT, Stage.RANDOM_ROULETTE_STAGE);
    }

    public static StageEvent selectEnd() {
        return new StageEvent(StageEventType.SELECT, Stage.ENDING_STAGE);
    }
}
