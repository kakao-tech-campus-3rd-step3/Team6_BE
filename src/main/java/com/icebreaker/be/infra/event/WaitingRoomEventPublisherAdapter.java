package com.icebreaker.be.infra.event;

import com.icebreaker.be.application.waitingroom.event.WaitingRoomEventPublisher;
import com.icebreaker.be.application.waitingroom.event.WaitingRoomFullEvent;
import com.icebreaker.be.application.waitingroom.event.WaitingRoomParticipantJoinedEvent;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class WaitingRoomEventPublisherAdapter extends EventPublisherSupport implements
        WaitingRoomEventPublisher {

    public WaitingRoomEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }

    @Override
    public void publishFulled(WaitingRoomWithParticipants roomWithParticipants) {
        publishEvent(new WaitingRoomFullEvent(roomWithParticipants));
    }

    @Override
    public void publishJoined(String roomId, WaitingRoomWithParticipants roomWithParticipants) {
        publishEvent(new WaitingRoomParticipantJoinedEvent(roomId, roomWithParticipants));
    }
}
