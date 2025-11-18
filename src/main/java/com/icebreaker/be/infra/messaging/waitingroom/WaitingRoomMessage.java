package com.icebreaker.be.infra.messaging.waitingroom;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoomStartedPayload.class, name = "ROOM_STARTED"),
        @JsonSubTypes.Type(value = ParticipantJoinedPayload.class, name = "PARTICIPANT_JOINED")
})
public sealed interface WaitingRoomMessage
        permits RoomStartedPayload, ParticipantJoinedPayload {

}