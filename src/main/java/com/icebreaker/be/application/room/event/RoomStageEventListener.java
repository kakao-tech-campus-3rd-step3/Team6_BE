package com.icebreaker.be.application.room.event;


import com.icebreaker.be.application.room.RoomStageService;
import com.icebreaker.be.domain.EventPublisher;
import com.icebreaker.be.global.annotation.AsyncTransactionalEventListener;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessage;
import com.icebreaker.be.infra.persistence.redis.message.RedisMessageType;
import com.icebreaker.be.infra.persistence.redis.message.RoomStageChangeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomStageEventListener {

    private final RoomStageService roomStageService;
    private final EventPublisher eventPublisher;

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRoomStageChangeEvent(RoomStageChangeEvent event) {
        roomStageService.changeStage(event.roomCode(), event.stage());

        RoomStageChangeMessage payload = new RoomStageChangeMessage(event.roomCode(),
                event.stage());
        PubSubMessage<RoomStageChangeMessage> message = new PubSubMessage<>(
                RedisMessageType.ROOM_STAGE_CHANGE, payload);
        eventPublisher.publish(message);
        log.info("Publishing ROOM_STAGE_CHANGE message to redis for roomCode: {}, new stage: {}",
                event.roomCode(), event.stage());
    }
}
