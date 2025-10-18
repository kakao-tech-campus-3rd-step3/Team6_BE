package com.icebreaker.be.application.game.dto;

import lombok.Getter;

@Getter
public final class TopicRecommendGameContext extends GameContext {

    private final String TopicName;

    public TopicRecommendGameContext(String roomCode, String getTopicName) {
        super(roomCode);
        this.TopicName = getTopicName;
    }
}
