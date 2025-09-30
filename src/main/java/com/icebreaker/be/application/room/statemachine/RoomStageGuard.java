package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEvent;

public interface RoomStageGuard {

    boolean canTransition(Stage current, StageEvent event);
}
