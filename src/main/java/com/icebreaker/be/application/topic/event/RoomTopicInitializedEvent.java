package com.icebreaker.be.application.topic.event;

public record RoomTopicInitializedEvent(String roomCode) {

    public static RoomTopicInitializedEvent of(String code) {
        return new RoomTopicInitializedEvent(code);
    }
}
