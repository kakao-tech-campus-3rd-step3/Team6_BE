package com.icebreaker.be.application.question.dto;

import com.icebreaker.be.domain.question.QuestionType;

public record QuestionResponseDto(
    Long id,
    String content,
    QuestionType type
) {

}
