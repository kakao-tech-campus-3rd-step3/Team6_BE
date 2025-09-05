package com.icebreaker.be.domain.room.vo;

import java.util.List;

public record WaitingRoomWithParticipantIds(
        WaitingRoomStatus status,
        WaitingRoom room,
        List<Long> participants
) {

}
