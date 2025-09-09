package com.icebreaker.be.application.waitingroom.notify;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // JSON에 여전히 구분자로 찍힘
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoomStartedPayload.class, name = "ROOM_STARTED"),
        @JsonSubTypes.Type(value = ParticipantJoinedPayload.class, name = "PARTICIPANT_JOINED")
})
public sealed interface WaitingRoomMessage
        permits RoomStartedPayload, ParticipantJoinedPayload {

}