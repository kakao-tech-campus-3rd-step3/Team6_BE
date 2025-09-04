package com.icebreaker.be.infra.generator;

import com.icebreaker.be.domain.room.service.WaitingRoomIdGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class InMemoryWaitingRoomIdGenerator implements WaitingRoomIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
