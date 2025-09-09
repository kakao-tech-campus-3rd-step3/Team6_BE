package com.icebreaker.be.application.waitingroom.notify;

public record RoomStartedPayload(
        String roomId
) implements WaitingRoomMessage {

}
