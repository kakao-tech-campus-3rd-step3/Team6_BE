package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoomStageTransitions {

    private final List<RoomStageTransition> transitions = new ArrayList<>();

    public static RoomStageTransitions create() {
        return new RoomStageTransitions();
    }

    public RoomStageTransitions transition(Stage from, Stage to, StageEvent event) {
        RoomStageTransition transition = RoomStageTransition.builder()
                .from(from)
                .to(to)
                .event(event)
                .build();

        transitions.add(transition);
        return this;
    }

    public RoomStageTransitions selectTransition(Stage from, Stage to) {
        return transition(from, to, StageEvent.select(to));
    }

    public RoomStageTransitions nextTransition(Stage from, Stage to) {
        return transition(from, to, StageEvent.next());
    }

    public RoomStageTransitions prevTransition(Stage from, Stage to) {
        return transition(from, to, StageEvent.prev());
    }

    public List<RoomStageTransition> build() {
        return List.copyOf(transitions);
    }
}
