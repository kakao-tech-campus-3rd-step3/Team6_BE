package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.application.room.event.StageTransitionEvent;
import com.icebreaker.be.domain.room.vo.Stage;

public record RoomStageTransitionKey(Stage stage, StageTransitionEvent event) {

}