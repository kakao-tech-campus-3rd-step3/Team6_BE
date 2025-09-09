package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants.Participant;
import java.util.List;

public record WaitingRoomParticipantJoinedEvent(
        String roomId,
        List<Participant> participants
) {

}
