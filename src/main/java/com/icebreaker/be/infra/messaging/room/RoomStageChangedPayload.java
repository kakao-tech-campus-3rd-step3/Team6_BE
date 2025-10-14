package com.icebreaker.be.infra.messaging.room;

import com.icebreaker.be.domain.room.vo.Stage;

public record RoomStageChangedPayload(
        Stage stage
) {

}
