package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEvent;

public record RoomStageTransitionKey(Stage stage, StageEvent event) {

}