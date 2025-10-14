package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.GameResult;
import com.icebreaker.be.domain.game.GameCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicRecommendGameHandler implements GameHandler {

    @Override
    public GameCategory getCategory() {
        return GameCategory.TOPIC_RECOMMEND;
    }

    @Override
    public GameResult handle() {
        //TODO: 주제 추천 게임 로직 구현 필요
        return new GameResult();
    }
}
