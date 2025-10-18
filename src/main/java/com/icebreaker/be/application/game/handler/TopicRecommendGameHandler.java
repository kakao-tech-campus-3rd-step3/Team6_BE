package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.messaging.GameNotifier;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.domain.topic.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopicRecommendGameHandler implements GameHandler {

    private final TopicRepository topicRepository;

    private final GameNotifier notifier;

    @Override
    public GameCategory getCategory() {
        return GameCategory.TOPIC_RECOMMEND;
    }

    @Override
    public void handle(String roomCode) {
        List<Topic> topics = topicRepository.findAllByRoom(roomCode);

//        notifier.notifyGameResult(roomCode, new GameResult<>(getCategory(), question));
    }
}
