package com.icebreaker.be.infra.persistence.redis.message;

import com.icebreaker.be.domain.room.vo.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomStageChangeMessage {

    private String roomCode;
    private Stage stage;
}
