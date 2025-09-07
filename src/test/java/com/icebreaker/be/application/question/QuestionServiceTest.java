package com.icebreaker.be.application.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.icebreaker.be.application.question.dto.CreateQuestionCommand;
import com.icebreaker.be.application.question.dto.QuestionId;
import com.icebreaker.be.application.question.dto.QuestionResponse;
import com.icebreaker.be.domain.question.Question;
import com.icebreaker.be.domain.question.QuestionRepository;
import com.icebreaker.be.domain.question.QuestionType;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionService 테스트")
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    @DisplayName("단건 질문 조회")
    void 단건질문_조회() {
        Long id = 3L;
        Question saved = new Question("content", QuestionType.COMMON);
        ReflectionTestUtils.setField(saved, "id", id);

        when(questionRepository.findById(id)).thenReturn(Optional.of(saved));

        QuestionResponse questionResponse = questionService.getQuestionById(id);

        assertThat(questionResponse.id()).isEqualTo(id);
        assertThat(questionResponse.content()).isEqualTo(saved.getContent());
        assertThat(questionResponse.type()).isEqualTo(saved.getType());
    }

    @Test
    @DisplayName("존재하지 않는 id로 질문 조회 시 오류")
    void 존재하지않는_ID로_질문_조회시_오류발생() {
        Long id = 1L;
        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> questionService.getQuestionById(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.QUESTION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("모든 질문 조회")
    void 전체_질문_조회() {
        Question saved1 = new Question("content1", QuestionType.COMMON);
        ReflectionTestUtils.setField(saved1, "id", 1L);
        Question saved2 = new Question("content2", QuestionType.PERSONAL);
        ReflectionTestUtils.setField(saved2, "id", 2L);

        when(questionRepository.findAll()).thenReturn(List.of(saved1, saved2));

        List<QuestionResponse> questionResponseList = questionService.getAllQuestions();

        assertThat(questionResponseList.size()).isEqualTo(2);
        assertThat(questionResponseList.get(0).content()).isEqualTo(saved1.getContent());
        assertThat(questionResponseList.get(0).type()).isEqualTo(saved1.getType());
        assertThat(questionResponseList.get(1).content()).isEqualTo(saved2.getContent());
        assertThat(questionResponseList.get(1).type()).isEqualTo(saved2.getType());
    }

    @Test
    @DisplayName("질문 생성")
    void 질문생성() {
        Long id = 3L;
        Question saved = new Question("content", QuestionType.COMMON);
        ReflectionTestUtils.setField(saved, "id", id);
        CreateQuestionCommand command = new CreateQuestionCommand("content", "공통");

        when(questionRepository.save(any(Question.class))).thenReturn(saved);

        QuestionId actual = questionService.createQuestion(command);

        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(id);
    }

    @Test
    @DisplayName("service삭제 호출 시 repository의 조회,삭제 호출")
    void 질문_삭제() {
        Long id = 3L;
        Question question = new Question("content", QuestionType.COMMON);
        ReflectionTestUtils.setField(question, "id", id);

        when(questionRepository.findById(id)).thenReturn(Optional.of(question));

        questionService.deleteQuestion(id);

        then(questionRepository).should(times(1)).findById(id);
        then(questionRepository).should(times(1)).delete(question);
    }
}