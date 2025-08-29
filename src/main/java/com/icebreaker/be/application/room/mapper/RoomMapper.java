package com.icebreaker.be.application.room.mapper;

import com.icebreaker.be.application.room.dto.RoomResponseDto;
import com.icebreaker.be.domain.room.Room;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoomMapper {

    public static RoomResponseDto toResponse(Room room) {
        return new RoomResponseDto(
                room.getId(),
                room.getName(),
                room.getMaxParticipants()
        );
    }
}
