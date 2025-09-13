package com.icebreaker.be.domain.question;

import static org.assertj.core.api.Assertions.assertThat;

import com.icebreaker.be.config.IntegrationTestSupport;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("QuestionRepository 통합 테스트")
class QuestionRepositoryIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문을 저장하고 조회할 수 있다")
    void saveAndFind_Success() {
        // given
        Question question = Question.builder()
                .content("당신의 취미는 무엇인가요?")
                .type(QuestionType.COMMON)
                .build();

        // when
        Question savedQuestion = questionRepository.save(question);
        Optional<Question> foundQuestion = questionRepository.findById(savedQuestion.getId());

        // then
        assertThat(foundQuestion).isPresent();
        assertThat(foundQuestion.get().getContent()).isEqualTo("당신의 취미는 무엇인가요?");
        assertThat(foundQuestion.get().getType()).isEqualTo(QuestionType.COMMON);
    }

    @Test
    @DisplayName("모든 질문을 조회할 수 있다")
    void findAll_Success() {
        // given
        Question question1 = Question.builder()
                .content("좋아하는 음식은?")
                .type(QuestionType.PERSONAL)
                .build();

        Question question2 = Question.builder()
                .content("여행 가고 싶은 곳은?")
                .type(QuestionType.COMMON)
                .build();

        entityManager.persist(question1);
        entityManager.persist(question2);
        entityManager.flush();

        // when
        List<Question> questions = questionRepository.findAll();

        // then
        assertThat(questions).hasSize(2);
        assertThat(questions).extracting(Question::getContent)
                .containsExactlyInAnyOrder("좋아하는 음식은?", "여행 가고 싶은 곳은?");
    }

    @Test
    @DisplayName("질문을 삭제할 수 있다")
    void delete_Success() {
        // given
        Question question = Question.builder()
                .content("삭제될 질문")
                .type(QuestionType.PERSONAL)
                .build();

        Question savedQuestion = questionRepository.save(question);
        Long questionId = savedQuestion.getId();

        // when
        questionRepository.delete(savedQuestion);
        entityManager.flush();

        // then
        Optional<Question> foundQuestion = questionRepository.findById(questionId);
        assertThat(foundQuestion).isEmpty();
    }

    @Test
    @DisplayName("질문 타입별로 저장되는지 확인할 수 있다")
    void saveByType_Success() {
        // given
        Question commonQuestion = Question.builder()
                .content("공통 질문")
                .type(QuestionType.COMMON)
                .build();

        Question personalQuestion = Question.builder()
                .content("개인 질문")
                .type(QuestionType.PERSONAL)
                .build();

        // when
        questionRepository.save(commonQuestion);
        questionRepository.save(personalQuestion);
        entityManager.flush();

        List<Question> allQuestions = questionRepository.findAll();

        // then
        assertThat(allQuestions).hasSize(2);
        assertThat(allQuestions)
                .extracting(Question::getType)
                .containsExactlyInAnyOrder(QuestionType.COMMON, QuestionType.PERSONAL);
    }
}
