package com.icebreaker.be.application.room.event;


import com.icebreaker.be.application.room.RoomStageService;
import com.icebreaker.be.application.room.notify.RoomStageWebSocketNotifier;
import com.icebreaker.be.domain.room.entity.Stage;
import com.icebreaker.be.global.annotation.AsyncTransactionalEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomEventListener {

    private final RoomStageService roomStageService;
    private final RoomStageWebSocketNotifier notifier;

    @EventListener
    public void handleRoomStageChangeEvent(RoomStageChangeEvent event) {
        roomStageService.changeStage(event.roomCode(), event.stage());
        notifier.notifyRoomStageChanged(event.roomCode(), event.stage());
    }

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRoomStageInitializedEvent(String roomCode) {
        roomStageService.initializeStage(roomCode);
        notifier.notifyRoomStageChanged(roomCode, Stage.STAGE_1);
    }
}
