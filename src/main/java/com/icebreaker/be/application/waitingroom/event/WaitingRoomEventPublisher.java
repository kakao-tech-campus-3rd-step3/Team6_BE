package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants.Participant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingRoomEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishFulled(WaitingRoomWithParticipants waitingRoomWithParticipants) {
        applicationEventPublisher.publishEvent(
                new WaitingRoomFullEvent(waitingRoomWithParticipants));
    }

    public void publishJoined(String roomId, List<Participant> participants) {
        applicationEventPublisher.publishEvent(
                new WaitingRoomParticipantJoinedEvent(roomId, participants));
    }
}