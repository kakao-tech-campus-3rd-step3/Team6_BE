package com.icebreaker.be.application.room.dto;

public record RoomResponse(
        Long id,
        String name,
        Integer maxParticipants
) {

}