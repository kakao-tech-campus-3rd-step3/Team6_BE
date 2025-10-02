package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.domain.room.repo.RoomStageRepository;
import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;
import com.icebreaker.be.domain.room.vo.StageTransitionEvent;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomStageStateMachine {

    private final RoomStageRepository stageRepository;
    private final Map<RoomStageTransitionKey, RoomStageTransition> transitionMap;

    public RoomStage transitionStage(String roomCode, StageTransitionEvent event) {
        RoomStage currentStage = loadOrInitCurrentStage(roomCode, event);

        if (event.type() == StageEventType.INIT) {
            return currentStage;
        }

        RoomStage targetStage = applyTransition(currentStage, event);
        stageRepository.save(targetStage);
        return targetStage;
    }

    private RoomStage loadOrInitCurrentStage(String roomCode, StageTransitionEvent event) {
        return stageRepository.findByRoomCode(roomCode)
                .orElseGet(() -> initStageIfEventIsInit(roomCode, event));
    }

    private RoomStage initStageIfEventIsInit(String roomCode, StageTransitionEvent event) {
        if (event.type() != StageEventType.INIT) {
            throw new BusinessException(ErrorCode.ROOM_STAGE_NOT_INITIALIZED);
        }

        RoomStage initialStage = new RoomStage(roomCode, Stage.PROFILE_VIEW_STAGE);
        stageRepository.save(initialStage);
        return initialStage;
    }

    private RoomStageTransition getTransition(Stage currentStage, StageTransitionEvent event) {

        RoomStageTransitionKey transitionKey = new RoomStageTransitionKey(currentStage, event);
        RoomStageTransition transition = transitionMap.get(transitionKey);

        if (transition == null) {
            throw new BusinessException(ErrorCode.INVALID_STAGE_TRANSITION);
        }

        return transition;
    }

    private RoomStage applyTransition(RoomStage roomStage, StageTransitionEvent event) {
        RoomStageTransition transition = getTransition(roomStage.stage(), event);

        return new RoomStage(roomStage.roomCode(), transition.to());
    }
}
