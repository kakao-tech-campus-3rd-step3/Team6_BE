package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.entity.Stage;

public record RoomStageChangeEvent(
        String roomCode,
        Stage stage
) {

}
