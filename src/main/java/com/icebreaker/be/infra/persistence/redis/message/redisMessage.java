package com.icebreaker.be.infra.persistence.redis.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class redisMessage<T> {

    private String type;
    private T message;
}
