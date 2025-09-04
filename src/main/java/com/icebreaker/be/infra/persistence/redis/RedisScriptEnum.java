package com.icebreaker.be.infra.persistence.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisScriptEnum {

    CREATE_ROOM("lua/create_room.lua"),
    JOIN_ROOM("lua/join_room.lua");

    private final String classpath;
}