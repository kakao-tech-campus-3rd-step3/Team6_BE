package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageTransitionEvent;

public record RoomStageTransitionKey(Stage stage, StageTransitionEvent event) {

}