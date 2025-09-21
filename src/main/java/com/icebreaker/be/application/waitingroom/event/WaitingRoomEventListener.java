package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.application.room.RoomService;
import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.global.annotation.AsyncTransactionalEventListener;
import com.icebreaker.be.infra.messaging.waitingroom.WaitingRoomWebSocketNotifier;
import com.icebreaker.be.infra.persistence.redis.message.ParticipantJoinedMessage;
import com.icebreaker.be.infra.persistence.redis.message.RoomStartedMessage;
import com.icebreaker.be.infra.persistence.redis.message.redisMessage;
import com.icebreaker.be.infra.persistence.redis.publisher.RedisPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class WaitingRoomEventListener {

    private final WaitingRoomWebSocketNotifier notifier;
    private final RoomService roomService;
    private final RedisPublisher redisPublisher;

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWaitingRoomParticipantJoinedEvent(WaitingRoomParticipantJoinedEvent event) {

        ParticipantJoinedMessage payload = new ParticipantJoinedMessage(event.roomId(),
                event.waitingRoomWithParticipants());
        redisMessage<ParticipantJoinedMessage> message = new redisMessage<>(
                "PARTICIPANT_JOINED", payload);
        redisPublisher.publish(message);
        log.info("PARTICIPANT_JOINED message publish to redis");
    }

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWaitingRoomFullEvent(WaitingRoomFullEvent event) {
        WaitingRoom waitingRoom = event.getWaitingRoom();
        List<Long> participantIds = event.getParticipantIds();

        roomService.createRoom(waitingRoom, participantIds);

        RoomStartedMessage payload = new RoomStartedMessage(waitingRoom.roomId());
        redisMessage<RoomStartedMessage> message = new redisMessage<>(
                "ROOM_STARTED", payload);
        redisPublisher.publish(message);
    }
}
