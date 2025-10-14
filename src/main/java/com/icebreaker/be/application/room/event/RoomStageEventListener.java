package com.icebreaker.be.application.room.event;


import com.icebreaker.be.application.room.RoomStageService;
import com.icebreaker.be.global.annotation.AsyncTransactionalEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomStageEventListener {

    private final RoomStageService roomStageService;

    @AsyncTransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRoomStageChangeEvent(RoomStageTransitionEvent event) {
        roomStageService.changeStage(event.roomCode(), event.stageEvent());
        log.info("Handled RoomStageChangeEvent for roomCode: {}, new stage: {}", event.roomCode(),
                event.targetStage());
    }
}
