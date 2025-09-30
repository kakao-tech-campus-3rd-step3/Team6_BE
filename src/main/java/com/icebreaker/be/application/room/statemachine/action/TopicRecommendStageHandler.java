package com.icebreaker.be.application.room.statemachine.action;

import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import org.springframework.stereotype.Component;

@Component
public class TopicRecommendStageHandler implements RoomStageActionHandler {

    @Override
    public Stage getStage() {
        return Stage.TOPIC_RECOMMEND_STAGE;
    }

    @Override
    public void handle(String roomCode, RoomStage stage) {

    }
}
