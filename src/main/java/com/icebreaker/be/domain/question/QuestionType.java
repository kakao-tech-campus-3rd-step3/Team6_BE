package com.icebreaker.be.domain.question;

import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;

public enum QuestionType {
  COMMON, PERSONAL;

  public static QuestionType fromString(String questionTypeString) {
    try {
      return QuestionType.valueOf(questionTypeString.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new BusinessException(ErrorCode.INVALID_QUESTION_TYPE);
    }
  }
}
