package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import com.icebreaker.be.domain.room.vo.WaitingRoomWithParticipantIds;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingRoomEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishFulled(WaitingRoomWithParticipantIds waitingRoomWithParticipantIds) {
        applicationEventPublisher.publishEvent(
                new WaitingRoomFullEvent(waitingRoomWithParticipantIds));
    }

    public void publishJoined(String roomId, WaitingRoomParticipant participant) {
        applicationEventPublisher.publishEvent(
                new WaitingRoomParticipantJoinedEvent(roomId, participant));
    }
}