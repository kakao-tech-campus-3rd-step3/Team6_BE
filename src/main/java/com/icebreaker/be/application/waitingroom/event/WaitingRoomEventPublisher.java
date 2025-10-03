package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;

public interface WaitingRoomEventPublisher {

    void publishFulled(WaitingRoomWithParticipants waitingRoomWithParticipants);

    void publishJoined(String roomId,
            WaitingRoomWithParticipants waitingRoomWithParticipants);
}