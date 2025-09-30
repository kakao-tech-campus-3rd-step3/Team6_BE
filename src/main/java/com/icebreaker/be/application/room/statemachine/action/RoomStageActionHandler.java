package com.icebreaker.be.application.room.statemachine.action;

import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;

public interface RoomStageActionHandler {

    Stage getStage();

    void handle(String roomCode, RoomStage stage);
}
