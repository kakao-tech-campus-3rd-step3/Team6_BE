package com.icebreaker.be.application.room;

import com.icebreaker.be.application.room.statemachine.RoomStageStateMachine;
import com.icebreaker.be.domain.room.vo.StageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomStageService {

    private final RoomStageStateMachine roomStageStateMachine;

    public void changeStage(String roomCode, StageEvent event) {
        roomStageStateMachine.transitionStage(roomCode, event);
    }
}
