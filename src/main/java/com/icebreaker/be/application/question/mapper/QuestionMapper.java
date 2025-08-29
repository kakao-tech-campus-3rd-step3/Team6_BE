package com.icebreaker.be.application.question.mapper;

import com.icebreaker.be.application.question.dto.QuestionResponse;
import com.icebreaker.be.domain.question.Question;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuestionMapper {

  public static QuestionResponse toResponse(Question question) {
    return new QuestionResponse(
        question.getId(),
        question.getContent(),
        question.getType()
    );
  }
}
