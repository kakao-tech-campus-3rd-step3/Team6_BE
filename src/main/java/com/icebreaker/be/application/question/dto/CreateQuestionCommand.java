package com.icebreaker.be.application.question.dto;

import com.icebreaker.be.domain.question.Question;
import com.icebreaker.be.domain.question.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateQuestionCommand(
        @NotBlank
        @Size(max = 255)
        @Schema(description = "질문 내용", example = "어떤 음식을 좋아하시나요?", maxLength = 255)
        String content,
        
        @NotBlank
        @Schema(description = "질문 유형", example = "공통")
        String questionType
) {

    public Question toEntity() {
        return Question.builder()
                .content(content)
                .type(QuestionType.fromString(questionType))
                .build();
    }
}
