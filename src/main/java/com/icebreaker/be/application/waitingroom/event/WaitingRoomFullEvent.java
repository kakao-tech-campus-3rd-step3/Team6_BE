package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipantIds;

public record WaitingRoomFullEvent(
        WaitingRoomWithParticipantIds waitingRoomWithParticipantIds
) {

}