package com.icebreaker.be.infra.persistence.redis.waitingroom;

import com.icebreaker.be.global.exception.ErrorCode;
import com.icebreaker.be.infra.persistence.redis.StatusMapper;
import org.springframework.stereotype.Component;

@Component
public class WaitingRoomStatusMapper implements StatusMapper<WaitingRoomResponseStatus> {

    @Override
    public ErrorCode toErrorCode(WaitingRoomResponseStatus status) {
        return switch (status) {
            case ROOM_NOT_FOUND -> ErrorCode.WAITING_ROOM_NOT_FOUND;
            case ALREADY_JOINED -> ErrorCode.ALREADY_ROOM_JOIN;
            case FULL -> ErrorCode.WAITING_ROOM_FULL;
            case ROOM_ALREADY_EXISTS -> ErrorCode.ROOM_ALREADY_EXISTS;
            default -> ErrorCode.INTERNAL_SERVER_ERROR;
        };
    }

    @Override
    public boolean isSuccess(WaitingRoomResponseStatus status) {
        return status.isSuccess();
    }

    @Override
    public Class<WaitingRoomResponseStatus> getStatusClass() {
        return WaitingRoomResponseStatus.class;
    }
}
