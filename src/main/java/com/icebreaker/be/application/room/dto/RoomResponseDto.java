package com.icebreaker.be.application.room.dto;

public record RoomResponseDto(
        Long id,
        String name,
        Integer maxParticipants
) {

}