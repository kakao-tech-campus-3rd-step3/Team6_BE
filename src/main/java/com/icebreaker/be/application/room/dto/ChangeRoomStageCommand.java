package com.icebreaker.be.application.room.dto;

import com.icebreaker.be.domain.room.entity.Stage;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;

public record ChangeRoomStageCommand(
        String stage
) {

    public Stage getStageEnum() {
        try {
            return Stage.valueOf(stage);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_STAGE_VALUE);
        }
    }
}
