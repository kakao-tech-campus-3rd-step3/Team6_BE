package com.icebreaker.be.application.room.statemachine;

import static com.icebreaker.be.domain.room.vo.Stage.ENDING_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.GAME_LIST_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.MANITTO_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.PROFILE_VIEW_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.RANDOM_ROULETTE_STAGE;
import static com.icebreaker.be.domain.room.vo.Stage.TOPIC_RECOMMEND_STAGE;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomStageStateMachineConfig {

    @Bean
    public RoomStageTransitions transitions() {
        return RoomStageTransitions.create()
                .nextTransition(PROFILE_VIEW_STAGE, GAME_LIST_STAGE)
                .selectTransition(GAME_LIST_STAGE, TOPIC_RECOMMEND_STAGE)
                .selectTransition(GAME_LIST_STAGE, RANDOM_ROULETTE_STAGE)
                .selectTransition(GAME_LIST_STAGE, MANITTO_STAGE)
                .selectTransition(GAME_LIST_STAGE, ENDING_STAGE)
                .prevTransition(MANITTO_STAGE, GAME_LIST_STAGE)
                .prevTransition(TOPIC_RECOMMEND_STAGE, GAME_LIST_STAGE)
                .prevTransition(RANDOM_ROULETTE_STAGE, GAME_LIST_STAGE)
                .build();
    }

    @Bean
    public Map<RoomStageTransitionKey, RoomStageTransition> transitionMap(
            RoomStageTransitions transitions) {
        return transitions.toTransitionMap();
    }
}
