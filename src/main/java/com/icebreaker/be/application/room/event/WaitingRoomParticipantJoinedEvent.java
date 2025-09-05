package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;

public record WaitingRoomParticipantJoinedEvent(
        String roomId,
        WaitingRoomParticipant participant
) {

}
