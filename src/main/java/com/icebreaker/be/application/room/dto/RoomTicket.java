package com.icebreaker.be.application.room.dto;

import com.icebreaker.be.domain.room.vo.RoomParticipantRole;

public record RoomTicket(
        Long userId,
        RoomParticipantRole role,
        String token
) {

}
