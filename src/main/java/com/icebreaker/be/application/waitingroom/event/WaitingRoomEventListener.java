package com.icebreaker.be.application.waitingroom.event;

import com.icebreaker.be.application.room.RoomService;
import com.icebreaker.be.domain.publisher.EventPublisher;
import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.global.annotation.AsyncTransactionalEventListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class WaitingRoomEventListener {

    private final RoomService roomService;
    private final EventPublisher eventPublisher;

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWaitingRoomParticipantJoinedEvent(WaitingRoomParticipantJoinedEvent event) {

        eventPublisher.publish(event);
        log.info("Publishing PARTICIPANT_JOINED message for roomId: {} publish to redis",
                event.roomId());
    }

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWaitingRoomFullEvent(WaitingRoomFullEvent event) {
        WaitingRoom waitingRoom = event.getWaitingRoom();
        List<Long> participantIds = event.getParticipantIds();

        roomService.createRoom(waitingRoom, participantIds);

        eventPublisher.publish(event);
        log.info("Publishing ROOM_STARTED message, participantIds: {} publish to redis",
                event.getParticipantIds());
    }
}
