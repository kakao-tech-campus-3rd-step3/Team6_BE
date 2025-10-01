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
        return select(Stage.TOPIC_RECOMMEND_STAGE);
    }

    public static StageEvent selectRandom() {
        return select(Stage.RANDOM_ROULETTE_STAGE);
    }

    public static StageEvent selectEnd() {
        return select(Stage.ENDING_STAGE);
    }
}
