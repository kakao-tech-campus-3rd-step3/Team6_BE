package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
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

    public void publishStageChanged(String roomCode, StageEventType eventType, Stage stage) {
        switch (eventType) {
            case NEXT -> publishStageNext(roomCode);
            case PREV -> publishStagePrevious(roomCode);
            case SELECT -> publishStageSelected(roomCode, stage);
            default -> throw new BusinessException(ErrorCode.INVALID_STAGE_EVENT_TYPE);
        }
    }
}