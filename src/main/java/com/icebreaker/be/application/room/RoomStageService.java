package com.icebreaker.be.application.room;

import com.icebreaker.be.application.room.messaging.RoomStageNotifier;
import com.icebreaker.be.application.room.statemachine.RoomStageStateMachine;
import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.StageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomStageService {

    private final RoomStageStateMachine roomStageStateMachine;
    private final RoomStageNotifier notifier;

    public void changeStage(String roomCode, StageEvent event) {
        RoomStage roomStage = roomStageStateMachine.transitionStage(roomCode, event);
        notifier.notifyRoomStageChanged(roomCode, roomStage.stage());
    }
}
