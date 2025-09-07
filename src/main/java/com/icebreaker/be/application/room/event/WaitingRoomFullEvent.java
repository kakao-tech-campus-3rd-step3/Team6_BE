package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.WaitingRoomWithParticipantIds;

public record WaitingRoomFullEvent(
        WaitingRoomWithParticipantIds waitingRoomWithParticipantIds
) {

}