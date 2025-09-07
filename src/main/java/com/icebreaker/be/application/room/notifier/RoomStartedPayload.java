package com.icebreaker.be.application.room.notifier;

public record RoomStartedPayload(
        String roomId
) implements WaitingRoomMessage {

}
