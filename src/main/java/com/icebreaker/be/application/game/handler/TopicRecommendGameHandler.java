package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.dto.BroadcastGameResult;
import com.icebreaker.be.application.game.dto.GameResult;
import com.icebreaker.be.application.game.dto.TopicRecommendGameContext;
import com.icebreaker.be.application.question.QuestionPoolService;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.domain.question.Question;
import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.domain.topic.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopicRecommendGameHandler implements GameHandler<TopicRecommendGameContext> {

    private static final int QUESTION_LIMIT = 10;

    private final TopicRepository topicRepository;
    private final QuestionPoolService questionPoolService;

    @Override
    public GameCategory getCategory() {
        return GameCategory.TOPIC_RECOMMEND;
    }

    @Override
    public GameResult handle(TopicRecommendGameContext ctx) {
        String roomCode = ctx.getRoomCode();
        String topicName = ctx.getGetTopicName();

        return isBlank(topicName)
                ? handleWithoutTopic(roomCode)
                : handleWithTopic(topicName);
    }

    private GameResult handleWithoutTopic(String roomCode) {
        List<Topic> topics = topicRepository.findAllByRoom(roomCode);

        Topic mainTopic = topics.getFirst();
        List<Question> questions = questionPoolService.getQuestions(mainTopic, QUESTION_LIMIT);

        return BroadcastGameResult.of(new TopicsWithQuestions(topics, questions));
    }

    private GameResult handleWithTopic(String topicName) {
        List<Question> questions = questionPoolService.getQuestions(Topic.of(topicName),
                QUESTION_LIMIT);
        return BroadcastGameResult.of(questions);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record TopicsWithQuestions(List<Topic> topics, List<Question> questions) {

    }
}
