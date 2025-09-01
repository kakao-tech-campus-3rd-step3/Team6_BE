package com.icebreaker.be.application.room;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingRoomEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishRoomFullEvent(String roomId) {
        applicationEventPublisher.publishEvent(new WaitingRoomFullEvent(roomId));
    }
}