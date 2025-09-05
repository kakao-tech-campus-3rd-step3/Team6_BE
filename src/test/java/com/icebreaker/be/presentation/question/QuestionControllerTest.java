package com.icebreaker.be.presentation.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import com.icebreaker.be.global.exception.MvcGlobalExceptionAdvice;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionController 테스트")
class QuestionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(questionController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .setControllerAdvice(new MvcGlobalExceptionAdvice())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("모든 질문을 조회할 수 있다")
    void getAllQuestions_Success() throws Exception {
        // given
        QuestionResponse question1 = new QuestionResponse(1L, "content1", QuestionType.COMMON);
        QuestionResponse question2 = new QuestionResponse(2L, "content2", QuestionType.PERSONAL);
        when(questionService.getAllQuestions()).thenReturn(List.of(question1, question2));

        // when & then
        mockMvc.perform(get("/api/v1/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("전체 질문 조회 성공"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].content").value("content1"))
                .andExpect(jsonPath("$.data[0].type").value("COMMON"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].content").value("content2"))
                .andExpect(jsonPath("$.data[1].type").value("PERSONAL"));
    }

    @Test
    @DisplayName("빈 질문 목록을 조회할 수 있다")
    void getAllQuestions_EmptyList() throws Exception {
        // given
        when(questionService.getAllQuestions()).thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/v1/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @DisplayName("ID로 질문을 조회할 수 있다")
    void getQuestionById_Success() throws Exception {
        // given
        Long questionId = 1L;
        QuestionResponse question = new QuestionResponse(questionId, "질문 내용", QuestionType.COMMON);
        when(questionService.getQuestionById(questionId)).thenReturn(question);

        // when & then
        mockMvc.perform(get("/api/v1/questions/{id}", questionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("단일 질문 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("질문 내용"))
                .andExpect(jsonPath("$.data.type").value("COMMON"));
    }

    @Test
    @DisplayName("존재하지 않는 ID로 질문을 조회하면 404 에러가 발생한다")
    void getQuestionById_NotFound() throws Exception {
        // given
        Long questionId = 999L;
        when(questionService.getQuestionById(questionId)).thenThrow(
                new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/v1/questions/{id}", questionId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("유효한 데이터로 질문을 생성할 수 있다")
    void createQuestion_Success() throws Exception {
        // given
        CreateQuestionCommand command = new CreateQuestionCommand("새로운 질문입니다", "공통");
        Long createdQuestionId = 1L;
        when(questionService.createQuestion(any(CreateQuestionCommand.class))).thenReturn(
                createdQuestionId);

        // when & then
        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/questions/1"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("질문 생성 성공"));
    }

    @Test
    @DisplayName("질문을 삭제할 수 있다")
    void deleteQuestion_Success() throws Exception {
        // given
        Long questionId = 1L;
        doNothing().when(questionService).deleteQuestion(questionId);

        // when & then
        mockMvc.perform(delete("/api/v1/questions/{id}", questionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("질문 삭제 성공"));
    }
}