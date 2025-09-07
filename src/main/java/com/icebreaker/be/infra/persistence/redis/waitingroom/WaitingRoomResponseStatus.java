package com.icebreaker.be.infra.persistence.redis.waitingroom;


import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum WaitingRoomResponseStatus {
    //성공
    CREATED,
    JOINED,

    //실패
    ROOM_ALREADY_EXISTS,
    ALREADY_JOINED,
    FULL,
    ROOM_NOT_FOUND,
    @JsonEnumDefaultValue
    UNKNOWN;

    public boolean isSuccess() {
        return this == CREATED || this == JOINED;
    }
}
