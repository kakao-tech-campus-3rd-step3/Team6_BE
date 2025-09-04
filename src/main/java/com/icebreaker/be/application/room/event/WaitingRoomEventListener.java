package com.icebreaker.be.application.room.event;

import com.icebreaker.be.application.room.RoomService;
import com.icebreaker.be.application.room.notifier.WaitingRoomWebSocketNotifier;
import com.icebreaker.be.domain.room.vo.WaitingRoom;
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

    private final WaitingRoomWebSocketNotifier notifier;

    private final RoomService roomService;

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWaitingRoomParticipantJoinedEvent(WaitingRoomParticipantJoinedEvent event) {
        notifier.notifyParticipantJoined(event.roomId(), event.participant());
    }

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWaitingRoomFullEvent(WaitingRoomFullEvent event) {
        var waitingRoomWithParticipants = event.waitingRoomWithParticipantIds();

        WaitingRoom waitingRoom = waitingRoomWithParticipants.room();
        List<Long> participantIds = waitingRoomWithParticipants.participants();
        
        roomService.create(waitingRoom, participantIds);

        notifier.notifyRoomStarted(waitingRoom.roomId());
    }
}
