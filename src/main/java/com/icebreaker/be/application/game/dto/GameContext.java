package com.icebreaker.be.application.game.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ManittoGameContext.class, name = "MANITTO"),
        @JsonSubTypes.Type(value = RandomRouletteGameContext.class, name = "RANDOM_ROULETTE"),
        @JsonSubTypes.Type(value = TopicRecommendGameContext.class, name = "TOPIC_RECOMMEND")
})
@RequiredArgsConstructor
@Getter
public sealed abstract class GameContext permits ManittoGameContext, RandomRouletteGameContext,
        TopicRecommendGameContext {

    private final String roomCode;
}
