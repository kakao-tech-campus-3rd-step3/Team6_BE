package com.icebreaker.be.application.question.mapper;

import com.icebreaker.be.application.question.dto.QuestionResponseDto;
import com.icebreaker.be.domain.question.Question;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuestionMapper {

  public static QuestionResponseDto toResponse(Question question) {
    return new QuestionResponseDto(
        question.getId(),
        question.getContent(),
        question.getType()
    );
  }
}
