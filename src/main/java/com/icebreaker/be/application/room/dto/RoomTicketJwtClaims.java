package com.icebreaker.be.application.room.dto;

import com.icebreaker.be.application.jwt.JwtClaims;
import com.icebreaker.be.domain.room.vo.RoomParticipantRole;
import java.util.Map;

public record RoomTicketJwtClaims(
        String roomCode,
        RoomParticipantRole role
) implements JwtClaims {

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "roomCode", roomCode,
                "role", role.name()
        );
    }
}
