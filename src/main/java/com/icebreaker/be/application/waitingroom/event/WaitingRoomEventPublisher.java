package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.domain.waitingroom.WaitingRoomParticipant;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipantIds;
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