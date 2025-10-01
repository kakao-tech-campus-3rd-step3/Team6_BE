package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEvent;
import lombok.Builder;

@Builder
public record RoomStageTransition(
        Stage from,
        Stage to,
        StageEvent event
) {

    public boolean isApplicable(Stage current, StageEvent event) {
        return from == current && this.event.equals(event);
    }
}
