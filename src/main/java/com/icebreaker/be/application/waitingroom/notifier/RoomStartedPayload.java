package com.icebreaker.be.application.waitingroom.notifier;

public record RoomStartedPayload(
        String roomId
) implements WaitingRoomMessage {

}
