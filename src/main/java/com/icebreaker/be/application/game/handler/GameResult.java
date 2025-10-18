package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.domain.game.GameCategory;

public record GameResult<T>(
        GameCategory category,
        T result
) {

    public static <R> GameResult<R> of(GameCategory category, R result) {
        return new GameResult<>(category, result);
    }
}
