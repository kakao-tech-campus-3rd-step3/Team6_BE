package com.icebreaker.be.application.topic.event;

import com.icebreaker.be.domain.room.vo.RoomParticipantInterest;
import java.util.List;

public interface TopicPreLoadEventPublisher {

    void publishInitialized(String roomCode,
            List<RoomParticipantInterest> roomParticipantInterests);
}
