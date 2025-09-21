package com.icebreaker.be.application.room.event;


import com.icebreaker.be.application.room.RoomStageService;
import com.icebreaker.be.global.annotation.AsyncTransactionalEventListener;
import com.icebreaker.be.infra.persistence.redis.message.RoomStageChangeMessage;
import com.icebreaker.be.infra.persistence.redis.message.redisMessage;
import com.icebreaker.be.infra.persistence.redis.publisher.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomStageEventListener {

    private final RoomStageService roomStageService;
    private final RedisPublisher redisPublisher;

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRoomStageChangeEvent(RoomStageChangeEvent event) {
        roomStageService.changeStage(event.roomCode(), event.stage());
        
        RoomStageChangeMessage payload = new RoomStageChangeMessage(event.roomCode(),
                event.stage());
        redisMessage<RoomStageChangeMessage> message = new redisMessage<>(
                "ROOM_STAGE_CHANGE", payload);
        redisPublisher.publish(message);
        log.info("ROOM_STAGE_CHANGE message publish to redis");
    }
}
