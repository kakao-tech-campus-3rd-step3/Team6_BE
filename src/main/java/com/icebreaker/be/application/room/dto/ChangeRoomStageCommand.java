package com.icebreaker.be.application.room.dto;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;

public record ChangeRoomStageCommand(
        String stage,
        String eventType
) {

    public Stage getStageEnum() {
        try {
            return Stage.valueOf(stage);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_STAGE_VALUE);
        }
    }

    public StageEventType getEventTypeEnum() {
        try {
            return StageEventType.valueOf(eventType);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_STAGE_EVENT_TYPE);
        }
    }
}
