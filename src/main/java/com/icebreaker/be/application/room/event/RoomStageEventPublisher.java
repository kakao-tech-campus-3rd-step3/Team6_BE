package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomStageEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishStageInitialized(String roomCode) {
        applicationEventPublisher.publishEvent(
                RoomStageEvent.init(roomCode)
        );
    }

    public void publishStageNext(String roomCode) {
        applicationEventPublisher.publishEvent(
                RoomStageEvent.next(roomCode)
        );
    }

    public void publishStagePrevious(String roomCode) {
        applicationEventPublisher.publishEvent(
                RoomStageEvent.prev(roomCode)
        );
    }

    public void publishStageSelected(String roomCode, Stage stage) {
        applicationEventPublisher.publishEvent(
                RoomStageEvent.select(roomCode, stage)
        );
    }
}