package com.icebreaker.be.infra.messaging.room;

import com.icebreaker.be.domain.room.entity.Stage;

public record RoomStageChangedPayload(
        Stage stage
) {

}
