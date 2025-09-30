package com.icebreaker.be.application.room.statemachine.action;

import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewStageHandler implements RoomStageActionHandler {

    @Override
    public Stage getStage() {
        return Stage.PROFILE_VIEW_STAGE;
    }

    @Override
    public void handle(String roomCode, RoomStage stage) {
        //참여자 조회 및 보여주기
    }
}
