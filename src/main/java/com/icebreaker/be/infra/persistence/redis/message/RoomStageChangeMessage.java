package com.icebreaker.be.infra.persistence.redis.message;

import com.icebreaker.be.domain.room.entity.Stage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomStageChangeMessage {

    private String roomCode;
    private Stage stage;
}
