package com.icebreaker.be.application.room.notifier;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RoomStartedMessage(
        @JsonProperty("type") WaitingRoomMessageType type,
        @JsonProperty("roomId") String roomId
) {

}
