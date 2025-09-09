package com.icebreaker.be.application.waitingroom.notify;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants.Participant;
import java.util.List;

public record ParticipantJoinedPayload(
        List<Participant> newParticipant
) implements WaitingRoomMessage {

}
