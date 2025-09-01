package com.icebreaker.be.infra.generator;

import com.icebreaker.be.domain.room.WaitingRoomIdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InMemoryWaitingRoomIdGenerator implements WaitingRoomIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
