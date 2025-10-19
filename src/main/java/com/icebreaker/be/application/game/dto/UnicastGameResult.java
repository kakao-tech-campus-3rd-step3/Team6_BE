package com.icebreaker.be.application.game.dto;

import java.util.List;

public record UnicastGameResult<T>(
        List<UserResult<T>> payload
) implements GameResult {

    public static <T> UnicastGameResult<T> of(List<UserResult<T>> payload) {
        return new UnicastGameResult<>(payload);
    }

    public record UserResult<T>(
            String userId,
            T payload
    ) {

    }
}
