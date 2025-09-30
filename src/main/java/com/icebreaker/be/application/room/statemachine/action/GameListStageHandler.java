package com.icebreaker.be.application.room.statemachine.action;

import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import org.springframework.stereotype.Component;

@Component
public class GameListStageHandler implements RoomStageActionHandler {

    @Override
    public Stage getStage() {
        return Stage.GAME_LIST_STAGE;
    }

    @Override
    public void handle(String roomCode, RoomStage stage) {

    }
}
