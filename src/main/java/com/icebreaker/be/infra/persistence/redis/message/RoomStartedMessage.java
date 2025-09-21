package com.icebreaker.be.infra.persistence.redis.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomStartedMessage {

    private String roomId;
}
