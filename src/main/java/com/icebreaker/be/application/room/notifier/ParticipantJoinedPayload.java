package com.icebreaker.be.application.room.notifier;

import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;

public record ParticipantJoinedPayload(
        WaitingRoomParticipant newParticipant
) implements WaitingRoomMessage {

}
