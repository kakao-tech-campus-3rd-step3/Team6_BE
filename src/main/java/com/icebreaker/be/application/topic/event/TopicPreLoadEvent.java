package com.icebreaker.be.application.topic.event;

import com.icebreaker.be.domain.room.vo.RoomParticipantInterest;
import java.util.List;

public record TopicPreLoadEvent(
        String roomCode,
        List<RoomParticipantInterest> participants
) {

}
