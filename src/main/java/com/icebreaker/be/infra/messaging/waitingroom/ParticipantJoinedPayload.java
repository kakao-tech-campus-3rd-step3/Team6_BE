package com.icebreaker.be.infra.messaging.waitingroom;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;

public record ParticipantJoinedPayload(
        WaitingRoomWithParticipants payload
) implements WaitingRoomMessage {

}
