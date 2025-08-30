package com.icebreaker.be.application.room.mapper;

import com.icebreaker.be.application.room.dto.RoomResponse;
import com.icebreaker.be.domain.room.Room;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoomMapper {

    public static RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getMaxParticipants()
        );
    }
}
