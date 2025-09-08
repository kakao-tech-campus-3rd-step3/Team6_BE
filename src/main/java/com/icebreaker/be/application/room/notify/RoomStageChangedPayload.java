package com.icebreaker.be.application.room.notify;

import com.icebreaker.be.domain.room.entity.Stage;

public record RoomStageChangedPayload(
        Stage stage
) {

}
