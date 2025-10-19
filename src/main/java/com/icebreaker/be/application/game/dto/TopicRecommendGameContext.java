package com.icebreaker.be.application.game.dto;

import lombok.Getter;

@Getter
public final class TopicRecommendGameContext extends GameContext {

    private final String topicName;

    public TopicRecommendGameContext(String roomCode, String topicName) {
        super(roomCode);
        this.topicName = topicName;
    }
}
