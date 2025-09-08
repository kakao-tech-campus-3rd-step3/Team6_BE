package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.entity.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishStageChanged(String roomCode, Stage stage) {
        applicationEventPublisher.publishEvent(
                RoomStageChangeEvent.of(roomCode, stage)
        );
    }
}