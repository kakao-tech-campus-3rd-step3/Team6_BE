package com.icebreaker.be.domain.question;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@DisplayName("QuestionRepository 테스트")
class QuestionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("id로 질문 단건 조회")
    void 단건질문_조회() {
        Question question = new Question("content", QuestionType.COMMON);
        Question savedQuestion = entityManager.persistAndFlush(question);

        Optional<Question> actualOptional = questionRepository.findById(savedQuestion.getId());

        assertThat(actualOptional).hasValueSatisfying(actual -> {
            assertThat(actual.getId()).isEqualTo(question.getId());
            assertThat(actual.getContent()).isEqualTo(question.getContent());
            assertThat(actual.getType()).isEqualTo(question.getType());
        });
    }

    @Test
    @DisplayName("존재하지 않는 id로 질문 조회")
    void 존재하지않는_ID로_질문_조회시_오류발생() {
        Optional<Question> question = questionRepository.findById(30L);

        assertThat(question).isEmpty();
    }

    @Test
    @DisplayName("전체 질문 조회")
    void 전체_질문_조회() {
        Question question1 = new Question("content1", QuestionType.COMMON);
        Question question2 = new Question("content2", QuestionType.PERSONAL);
        Question saved1 = entityManager.persistAndFlush(question1);
        Question saved2 = entityManager.persistAndFlush(question2);

        List<Question> questionList = questionRepository.findAll();

        assertThat(questionList.size()).isEqualTo(2);

        assertThat(questionList.get(0).getId()).isEqualTo(saved1.getId());
        assertThat(questionList.get(0).getContent()).isEqualTo(saved1.getContent());
        assertThat(questionList.get(0).getType()).isEqualTo(saved1.getType());
        assertThat(questionList.get(1).getId()).isEqualTo(saved2.getId());
        assertThat(questionList.get(1).getContent()).isEqualTo(saved2.getContent());
        assertThat(questionList.get(1).getType()).isEqualTo(saved2.getType());
    }

    @Test
    @DisplayName("질문 생성")
    void 질문생성() {
        Question question = new Question("content", QuestionType.COMMON);

        Question savedQuestion = questionRepository.save(question);

        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion.getContent()).isEqualTo(question.getContent());
        assertThat(savedQuestion.getType()).isEqualTo(question.getType());
    }

    @Test
    @DisplayName("질문 삭제")
    void 질문삭제() {
        Question question = new Question("content", QuestionType.COMMON);
        Question savedQuestion = entityManager.persistAndFlush(question);

        questionRepository.deleteById(savedQuestion.getId());
        Optional<Question> actual = questionRepository.findById(savedQuestion.getId());

        assertThat(actual).isEmpty();
    }
}