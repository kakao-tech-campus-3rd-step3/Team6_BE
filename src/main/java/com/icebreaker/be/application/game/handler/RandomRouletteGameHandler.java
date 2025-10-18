package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.dto.BroadcastGameResult;
import com.icebreaker.be.application.game.dto.GameResult;
import com.icebreaker.be.application.game.dto.RandomRouletteGameContext;
import com.icebreaker.be.application.question.QuestionPoolService;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.domain.question.Question;
import com.icebreaker.be.domain.room.repo.RoomParticipantRepository;
import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.domain.topic.TopicRepository;
import com.icebreaker.be.domain.user.User;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RandomRouletteGameHandler implements GameHandler<RandomRouletteGameContext> {

    private final RoomParticipantRepository roomParticipantRepository;
    private final TopicRepository topicRepository;
    private final QuestionPoolService questionPoolService;

    @Override
    public GameCategory getCategory() {
        return GameCategory.RANDOM_ROULETTE;
    }

    @Override
    public GameResult handle(RandomRouletteGameContext ctx) {
        String roomCode = ctx.getRoomCode();

        List<User> participants = roomParticipantRepository.findUsersByRoomCode(roomCode);
        User randomUser = findRandomUser(participants);
        List<Topic> topics = topicRepository.findByRoomAndUser(roomCode, randomUser.getId());
        Topic randomTopic = findRandomTopic(topics);

        Question question = questionPoolService.getQuestion(randomTopic);
        UserWithQuestion userWithQuestion = new UserWithQuestion(randomUser.getName(), question);

        log.info("[{}][{}] 랜덤 룰렛 게임 질문 생성 완료: {}", roomCode, getCategory(), question.content());
        return BroadcastGameResult.of(userWithQuestion);
    }

    private User findRandomUser(List<User> participants) {
        int randomIndex = ThreadLocalRandom.current().nextInt(participants.size());
        return participants.get(randomIndex);
    }

    private Topic findRandomTopic(List<Topic> topics) {
        int randomIndex = ThreadLocalRandom.current().nextInt(topics.size());
        return topics.get(randomIndex);
    }

    public record UserWithQuestion(String userName, Question question) {

    }
}
