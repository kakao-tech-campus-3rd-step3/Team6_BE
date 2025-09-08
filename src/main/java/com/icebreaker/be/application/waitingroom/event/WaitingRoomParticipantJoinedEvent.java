package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.domain.waitingroom.WaitingRoomParticipant;

public record WaitingRoomParticipantJoinedEvent(
        String roomId,
        WaitingRoomParticipant participant
) {

}
