package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.GameResult;
import com.icebreaker.be.domain.game.GameCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RandomRouletteGameHandler implements GameHandler {

    @Override
    public GameCategory getCategory() {
        return GameCategory.RANDOM_ROULETTE;
    }

    @Override
    public GameResult handle() {
        log.info("RandomRouletteGameHandler handling the game logic for RANDOM_ROULETTE_STAGE");
        return new GameResult();
    }
}
