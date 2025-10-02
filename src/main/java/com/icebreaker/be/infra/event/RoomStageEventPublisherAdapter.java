package com.icebreaker.be.infra.event;

import com.icebreaker.be.application.room.event.RoomStageEventPublisher;
import com.icebreaker.be.application.room.event.RoomStageTransitionEvent;
import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class RoomStageEventPublisherAdapter extends EventPublisherSupport implements
        RoomStageEventPublisher {


    public RoomStageEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }

    public void publishStageInitialized(String roomCode) {
        publishEvent(RoomStageTransitionEvent.init(roomCode));
    }

    public void publishStageChanged(String roomCode, StageEventType eventType, Stage stage) {
        switch (eventType) {
            case NEXT -> publishEvent(RoomStageTransitionEvent.next(roomCode));
            case PREV -> publishEvent(RoomStageTransitionEvent.prev(roomCode));
            case SELECT -> publishEvent(RoomStageTransitionEvent.select(roomCode, stage));
            default -> throw new BusinessException(ErrorCode.INVALID_STAGE_EVENT_VALUE);
        }
    }
}
