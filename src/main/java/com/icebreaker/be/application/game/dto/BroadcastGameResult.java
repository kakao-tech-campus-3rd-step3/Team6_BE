package com.icebreaker.be.application.game.dto;

public record BroadcastGameResult<T>(
        T payload
) implements GameResult {

    public static <T> BroadcastGameResult<T> of(T payload) {
        return new BroadcastGameResult<>(payload);
    }
}
