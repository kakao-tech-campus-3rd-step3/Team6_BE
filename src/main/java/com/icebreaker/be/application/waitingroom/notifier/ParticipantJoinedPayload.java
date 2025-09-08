package com.icebreaker.be.application.waitingroom.notifier;

import com.icebreaker.be.domain.waitingroom.WaitingRoomParticipant;

public record ParticipantJoinedPayload(
        WaitingRoomParticipant newParticipant
) implements WaitingRoomMessage {

}
