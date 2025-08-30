package com.icebreaker.be.presentation.question;

import com.icebreaker.be.application.question.QuestionService;
import com.icebreaker.be.application.question.dto.CreateQuestionCommand;
import com.icebreaker.be.application.question.dto.QuestionResponse;
import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.SuccessApiResponse;
import com.icebreaker.be.global.common.util.UriUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public final class QuestionController implements QuestionApiDocs {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<SuccessApiResponse<List<QuestionResponse>>> getAllQuestions() {
        List<QuestionResponse> questions = questionService.getAllQuestions();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(questions, "전체 질문 조회 성공"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessApiResponse<QuestionResponse>> getQuestionById(
            @PathVariable Long id
    ) {
        QuestionResponse question = questionService.getQuestionById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(question, "단일 질문 조회 성공"));
    }

    @PostMapping
    public ResponseEntity<SuccessApiResponse<Void>> createQuestion(
            @Valid @RequestBody CreateQuestionCommand cmd
    ) {
        Long questionId = questionService.createQuestion(cmd);
        return ResponseEntity
                .created(UriUtils.buildLocationUri(questionId))
                .body(ApiResponseFactory.success("질문 생성 성공"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessApiResponse<Void>> deleteQuestion(
            @PathVariable Long id
    ) {
        questionService.deleteQuestion(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponseFactory.success("질문 삭제 성공"));
    }
}
