package com.icebreaker.be.domain.waitingroom;

import java.util.List;

public record WaitingRoomWithParticipants(
        WaitingRoomStatus status,
        WaitingRoom room,
        List<Participant> participants
) {

    public record Participant(
            Long id,
            String name
    ) {

    }
}
