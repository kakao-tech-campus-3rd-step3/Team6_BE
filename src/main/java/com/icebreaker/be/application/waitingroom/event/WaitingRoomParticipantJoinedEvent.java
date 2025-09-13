package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;

public record WaitingRoomParticipantJoinedEvent(
        String roomId,
        WaitingRoomWithParticipants waitingRoomWithParticipants
) {

}
