package com.icebreaker.be.application.room.event;


import com.icebreaker.be.application.room.RoomStageService;
import com.icebreaker.be.global.annotation.AsyncTransactionalEventListener;
import com.icebreaker.be.infra.messaging.room.RoomStageWebSocketNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomStageEventListener {

    private final RoomStageService roomStageService;
    private final RoomStageWebSocketNotifier notifier;

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRoomStageChangeEvent(RoomStageChangeEvent event) {
        roomStageService.changeStage(event.roomCode(), event.stage());
        notifier.notifyRoomStageChanged(event.roomCode(), event.stage());

        log.info("Handled RoomStageChangeEvent for roomCode: {}, new stage: {}", event.roomCode(),
                event.stage());
    }
}
