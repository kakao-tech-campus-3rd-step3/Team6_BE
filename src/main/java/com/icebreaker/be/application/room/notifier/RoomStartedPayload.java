package com.icebreaker.be.application.room.notifier;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RoomStartedPayload(
        @JsonProperty("type") WaitingRoomMessageType type,
        @JsonProperty("roomId") String roomId
) {

}
