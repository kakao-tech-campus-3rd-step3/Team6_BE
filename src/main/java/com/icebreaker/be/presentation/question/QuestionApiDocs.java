package com.icebreaker.be.presentation.question;

import com.icebreaker.be.application.question.dto.CreateQuestionCommand;
import com.icebreaker.be.application.question.dto.QuestionResponse;
import com.icebreaker.be.global.common.response.SuccessApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Question", description = "질문 관리 API")
public sealed interface QuestionApiDocs permits QuestionController {

    @Operation(
            summary = "전체 질문 조회",
            description = "등록된 모든 질문 정보를 조회합니다."
    )
    ResponseEntity<SuccessApiResponse<List<QuestionResponse>>> getAllQuestions();

    @Operation(
            summary = "단일 질문 조회",
            description = "ID를 통해 특정 질문 정보를 조회합니다."
    )
    ResponseEntity<SuccessApiResponse<QuestionResponse>> getQuestionById(
            @Parameter(description = "질문 ID", required = true, example = "1")
            @PathVariable Long id
    );

    @Operation(
            summary = "질문 생성",
            description = "새로운 질문을 생성합니다."
    )
    ResponseEntity<SuccessApiResponse<Void>> createQuestion(
            @Parameter(description = "질문 생성 정보", required = true)
            @RequestBody CreateQuestionCommand cmd
    );

    @Operation(
            summary = "질문 삭제",
            description = "ID를 통해 특정 질문을 삭제합니다."
    )
    ResponseEntity<SuccessApiResponse<Void>> deleteQuestion(
            @Parameter(description = "삭제할 질문 ID", required = true, example = "1")
            @PathVariable Long id
    );
}
