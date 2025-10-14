package com.icebreaker.be.application.room.statemachine;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageTransitionEvent;
import com.icebreaker.be.global.common.util.CollectorsUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoomStageTransitions {

    private final List<RoomStageTransition> transitions = new ArrayList<>();

    public static RoomStageTransitions create() {
        return new RoomStageTransitions();
    }

    public RoomStageTransitions transition(Stage from, Stage to, StageTransitionEvent event) {
        RoomStageTransition transition = RoomStageTransition.builder()
                .from(from)
                .to(to)
                .event(event)
                .build();

        transitions.add(transition);
        return this;
    }

    public RoomStageTransitions selectTransition(Stage from, Stage to) {
        return transition(from, to, StageTransitionEvent.select(to));
    }

    public RoomStageTransitions nextTransition(Stage from, Stage to) {
        return transition(from, to, StageTransitionEvent.next());
    }

    public RoomStageTransitions prevTransition(Stage from, Stage to) {
        return transition(from, to, StageTransitionEvent.prev());
    }

    public RoomStageTransitions build() {
        return this;
    }

    /**
     * Convert the list of transitions to a map for efficient lookup.
     */
    public Map<RoomStageTransitionKey, RoomStageTransition> toTransitionMap() {
        return transitions.stream()
                .collect(CollectorsUtils.toMapByKey(
                        t -> new RoomStageTransitionKey(t.from(), t.event())));
    }
}
