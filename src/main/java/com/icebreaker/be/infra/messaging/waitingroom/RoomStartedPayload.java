package com.icebreaker.be.infra.messaging.waitingroom;

public record RoomStartedPayload(
        String roomId
) implements WaitingRoomMessage {

}
