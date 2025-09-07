package com.icebreaker.be.application.question.dto;

public record QuestionId(
        Long id
) {

    public static QuestionId of(Long id) {
        return new QuestionId(id);
    }
}
