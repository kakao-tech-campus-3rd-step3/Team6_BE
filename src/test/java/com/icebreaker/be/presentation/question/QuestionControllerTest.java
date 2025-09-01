package com.icebreaker.be.presentation.question;

import com.icebreaker.be.application.question.QuestionService;
import com.icebreaker.be.application.question.dto.QuestionResponse;
import com.icebreaker.be.domain.question.QuestionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionController 테스트")
class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @Test
    @DisplayName("QuestionController 테스트")
    void questionControllerTest() {
        // given
        QuestionResponse question1 = new QuestionResponse(1L, "content1", QuestionType.COMMON);
        QuestionResponse question2 = new QuestionResponse(2L, "content2", QuestionType.PERSONAL);
        when(questionService.getAllQuestions()).thenReturn(List.of(question1, question2));

        // when & then - 예외가 발생하지 않는지만 확인
        questionController.getAllQuestions();
    }
}