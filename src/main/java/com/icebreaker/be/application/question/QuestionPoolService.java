package com.icebreaker.be.application.question;

import com.icebreaker.be.application.question.llm.QuestionGenerator;
import com.icebreaker.be.application.question.llm.QuestionStore;
import com.icebreaker.be.domain.question.Question;
import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionPoolService {

    private final QuestionGenerator questionGenerator;
    private final QuestionStore questionStore;

    public List<Question> getQuestions(Topic topic, int count) {
        return questionStore.getSimilarQuestions(topic.name(), count);
    }

    public Question getQuestion(Topic topic) {
        return questionStore.getSimilarQuestions(topic.name(), 1)
                .stream()
                .findFirst()
                .orElseGet(() -> generateFallbackQuestion(topic));
    }

    public void buildQuestionPoolFor(Topic topic) {
        log.info("[{}] 질문 풀 재구성 시작", topic.name());
        List<Question> newQuestions = generateNewQuestions(topic);
        persistNewEmbeddings(newQuestions);

        log.info("[{}] 질문 풀 재구성 완료 ({}개 생성)", topic.name(), newQuestions.size());
    }


    public void clearOldQuestionPool(Topic topic) {
        questionStore.resetStore();
        log.debug("[{}] 기존 질문 풀 초기화 완료 (임시)", topic.name());
    }

    private Question generateFallbackQuestion(Topic topic) {
        List<Question> generated = generateNewQuestions(topic);

        persistNewEmbeddings(generated);

        return questionStore.getSimilarQuestions(topic.name(), 1)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_GENERATION_FAILED));
    }

    private List<Question> generateNewQuestions(Topic topic) {
        List<Question> generated = questionGenerator.generateQuestions(Set.of(topic));
        log.debug("[{}] 새로운 질문 {}개 생성", topic.name(), generated.size());
        return generated;
    }

    private void persistNewEmbeddings(List<Question> questions) {
        questionStore.saveAllEmbeddings(questions);
    }
}