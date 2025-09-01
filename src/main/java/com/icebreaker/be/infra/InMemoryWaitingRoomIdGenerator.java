package com.icebreaker.be.infra;

import com.icebreaker.be.domain.room.WaitingRoomIdGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class InMemoryWaitingRoomIdGenerator implements WaitingRoomIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
