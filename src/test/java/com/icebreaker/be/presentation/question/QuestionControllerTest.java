package com.icebreaker.be.presentation.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.application.question.QuestionService;
import com.icebreaker.be.application.question.dto.CreateQuestionCommand;
import com.icebreaker.be.application.question.dto.QuestionResponse;
import com.icebreaker.be.domain.question.QuestionType;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuestionController.class)
@DisplayName("QuestionController 테스트")
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private QuestionService questionService;

    @Test
    @DisplayName("모든 질문을 조회할 수 있다.")
    void getAllQuestions_success() throws Exception {
        QuestionResponse question1 = new QuestionResponse(1L, "content1", QuestionType.COMMON);
        QuestionResponse question2 = new QuestionResponse(2L, "content2", QuestionType.PERSONAL);

        when(questionService.getAllQuestions()).thenReturn(List.of(question1, question2));

        mockMvc.perform(get("/api/v1/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("전체 질문 조회 성공"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].content").value("content1"))
                .andExpect(jsonPath("$.data[0].type").value("COMMON"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].content").value("content2"))
                .andExpect(jsonPath("$.data[1].type").value("PERSONAL"));
    }

    @Test
    @DisplayName("ID로 질문을 단일 조회할 수 있다.")
    void getQuestionById_success() throws Exception {
        QuestionResponse question = new QuestionResponse(1L, "content1", QuestionType.COMMON);

        when(questionService.getQuestionById(question.id())).thenReturn(question);

        mockMvc.perform(get("/api/v1/questions/{id}", question.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("단일 질문 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.content").value("content1"))
                .andExpect(jsonPath("$.data.type").value("COMMON"));
    }

    @Test
    @DisplayName("존재하지 않는 ID로 질문을 조회하면 404 에러가 발생한다")
    void getQuestionById_notFound() throws Exception {
        Long questionId = 1L;
        when(questionService.getQuestionById(questionId))
                .thenThrow(new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        mockMvc.perform(get("/api/v1/questions/{id}", questionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("질문을 생성할 수 있다.")
    void createQuestion_success() throws Exception {
        CreateQuestionCommand questionCommand = new CreateQuestionCommand("content", "공통");
        Long questionId = 1L;

        when(questionService.createQuestion(any(CreateQuestionCommand.class))).thenReturn(
                questionId);

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("질문 생성 성공"))
                .andExpect(header().string("Location",
                        "http://localhost/api/v1/questions/" + questionId));
    }

    @Test
    @DisplayName("질문 타입을 한글로 작성하지 않으면 400 에러가 발생한다")
    void createQuestion_notKoreanQuestionType() throws Exception {
        CreateQuestionCommand questionCommand = new CreateQuestionCommand("content", "PERSONAL");
        Long questionId = 1L;

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("질문 내용을 작성하지 않으면 400 에러가 발생한다")
    void createQuestion_noQuestionContent() throws Exception {
        CreateQuestionCommand questionCommand = new CreateQuestionCommand(null, "공통");
        Long questionId = 1L;

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("질문 내용이 255글자를 넘기면 400 에러가 발생한다")
    void createQuestion_longQuestionContent() throws Exception {
        CreateQuestionCommand questionCommand = new CreateQuestionCommand("a".repeat(256),
                "공통");
        Long questionId = 1L;

        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionCommand)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("질문을 삭제할 수 있다")
    void deleteQuestion_success() throws Exception {
        Long questionId = 1L;

        mockMvc.perform(delete("/api/v1/questions/{id}", questionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("질문 삭제 성공"));
    }


    @Test
    @DisplayName("존재하지 않는 질문 삭제하려고 하면 404 에러가 발생한다")
    void deleteQuestion_notFound() throws Exception {
        Long questionId = 1L;

        doThrow(new BusinessException(ErrorCode.QUESTION_NOT_FOUND))
                .when(questionService).deleteQuestion(questionId);

        mockMvc.perform(delete("/api/v1/questions/{id}", questionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."));
    }
}