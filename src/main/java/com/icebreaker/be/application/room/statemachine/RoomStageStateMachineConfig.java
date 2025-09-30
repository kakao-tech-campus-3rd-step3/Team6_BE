package com.icebreaker.be.application.room.statemachine;

import static com.icebreaker.be.domain.room.vo.Stage.ENDING_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.GAME_LIST_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.MANITTO_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.PROFILE_VIEW_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.RANDOM_ROULETTE_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.TOPIC_RECOMMEND_STAGE;

import com.icebreaker.be.domain.room.vo.StageEvent;
import com.icebreaker.be.global.common.util.CollectorsUtils;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomStageStateMachineConfig {

    @Bean
    public List<RoomStageTransition> transitions() {
        return RoomStageTransitions.create()
                .transition(PROFILE_VIEW_STAGE, GAME_LIST_STAGE, StageEvent.next())
                .transition(GAME_LIST_STAGE, TOPIC_RECOMMEND_STAGE, StageEvent.selectTopic())
                .transition(GAME_LIST_STAGE, RANDOM_ROULETTE_STAGE, StageEvent.selectRandom())
                .transition(GAME_LIST_STAGE, ENDING_STAGE, StageEvent.selectEnd())
                .transition(MANITTO_STAGE, GAME_LIST_STAGE, StageEvent.prev())
                .transition(TOPIC_RECOMMEND_STAGE, GAME_LIST_STAGE, StageEvent.prev())
                .transition(RANDOM_ROULETTE_STAGE, GAME_LIST_STAGE, StageEvent.prev())
                .build();
    }

    @Bean
    public Map<RoomStageTransitionKey, RoomStageTransition> transactionMap() {
        return transitions().stream()
                .collect(CollectorsUtils.toMapByKey(
                        t -> new RoomStageTransitionKey(t.from(), t.event())));
    }
}
