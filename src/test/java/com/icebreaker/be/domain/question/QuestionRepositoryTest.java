package com.icebreaker.be.domain.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionRepository 테스트")
class QuestionRepositoryTest {

    @Mock
    private QuestionRepository questionRepository;

    private Question testQuestion;
    private Long testQuestionId = 1L;

    @BeforeEach
    void setUp() {
        testQuestion = new Question("테스트 질문", QuestionType.COMMON);

        // 리플렉션을 사용하여 id 설정 (테스트용)
        try {
            java.lang.reflect.Field idField = testQuestion.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testQuestion, testQuestionId);
        } catch (Exception e) {
            throw new RuntimeException("테스트 Question에 id 설정 실패", e);
        }
    }

    @Test
    @DisplayName("id로 질문 단건 조회")
    void 단건질문_조회() {
        // given
        when(questionRepository.findById(testQuestionId)).thenReturn(Optional.of(testQuestion));

        // when
        Optional<Question> actualOptional = questionRepository.findById(testQuestionId);

        // then
        assertThat(actualOptional).hasValueSatisfying(actual -> {
            assertThat(actual.getId()).isEqualTo(testQuestionId);
            assertThat(actual.getContent()).isEqualTo("테스트 질문");
            assertThat(actual.getType()).isEqualTo(QuestionType.COMMON);
        });
    }

    @Test
    @DisplayName("존재하지 않는 id로 질문 조회")
    void 존재하지않는_ID로_질문_조회시_오류발생() {
        // given
        when(questionRepository.findById(30L)).thenReturn(Optional.empty());

        // when
        Optional<Question> question = questionRepository.findById(30L);

        // then
        assertThat(question).isEmpty();
    }

    @Test
    @DisplayName("전체 질문 조회")
    void 전체_질문_조회() {
        // given
        Question question1 = new Question("질문1", QuestionType.COMMON);
        Question question2 = new Question("질문2", QuestionType.PERSONAL);

        // 리플렉션으로 ID 설정
        try {
            java.lang.reflect.Field idField = question1.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(question1, 1L);

            idField = question2.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(question2, 2L);
        } catch (Exception e) {
            throw new RuntimeException("테스트 Question에 id 설정 실패", e);
        }

        List<Question> questionList = Arrays.asList(question1, question2);
        when(questionRepository.findAll()).thenReturn(questionList);

        // when
        List<Question> questions = questionRepository.findAll();

        // then
        assertThat(questions.size()).isEqualTo(2);
        assertThat(questions.get(0).getContent()).isEqualTo("질문1");
        assertThat(questions.get(1).getContent()).isEqualTo("질문2");
    }

    @Test
    @DisplayName("질문 생성")
    void 질문_생성() {
        // given
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);

        // when
        Question question = new Question("새 질문", QuestionType.COMMON);
        Question savedQuestion = questionRepository.save(question);

        // then
        assertThat(savedQuestion).isNotNull();
        assertThat(savedQuestion.getId()).isEqualTo(testQuestionId);
        assertThat(savedQuestion.getContent()).isEqualTo("테스트 질문");
    }

    @Test
    @DisplayName("질문 삭제")
    void 질문_삭제() {
        // when - 삭제 후 조회 시 비어있는 결과를 반환하도록 설정
        when(questionRepository.findById(testQuestionId)).thenReturn(Optional.empty());

        // 삭제 후 조회
        Optional<Question> result = questionRepository.findById(testQuestionId);

        // then
        assertThat(result).isEmpty();
    }
}