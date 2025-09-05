package com.icebreaker.be.application.room.notifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;

public record ParticipantJoinedPayload(
        @JsonProperty("type") WaitingRoomMessageType type,
        @JsonProperty("newParticipant") WaitingRoomParticipant newParticipant
) {

}
