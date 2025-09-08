package com.icebreaker.be.domain.waitingroom;

import java.util.List;

public record WaitingRoomWithParticipantIds(
        WaitingRoomStatus status,
        WaitingRoom room,
        List<Long> participants
) {

}
