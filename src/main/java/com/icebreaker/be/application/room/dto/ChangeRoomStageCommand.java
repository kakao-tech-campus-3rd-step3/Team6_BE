package com.icebreaker.be.application.room.dto;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import jakarta.annotation.Nullable;

public record ChangeRoomStageCommand(
        String eventType,
        @Nullable String stage
) {

    public Stage getStageEnum() {
        if (stage == null) {
            return null;
        }
        try {
            return Stage.valueOf(stage.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_STAGE_EVENT_VALUE);
        }
    }

    public StageEventType getEventTypeEnum() {
        try {
            return StageEventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException(ErrorCode.INVALID_STAGE_EVENT_VALUE);
        }
    }
}
