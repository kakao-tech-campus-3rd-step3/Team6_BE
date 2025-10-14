package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageTransitionEvent;
import lombok.Builder;

@Builder
public record RoomStageTransition(
        Stage from,
        Stage to,
        StageTransitionEvent event
) {

}
