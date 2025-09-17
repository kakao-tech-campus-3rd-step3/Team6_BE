package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants.Participant;
import java.util.List;

public record WaitingRoomFullEvent(
        WaitingRoomWithParticipants waitingRoomWithParticipants
) {

    public WaitingRoom getWaitingRoom() {
        return waitingRoomWithParticipants.room();
    }

    public List<Long> getParticipantIds() {
        return waitingRoomWithParticipants.participants()
                .stream()
                .map(Participant::id)
                .toList();
    }
}