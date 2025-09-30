package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.GameResult;
import com.icebreaker.be.domain.game.GameCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ManittoGameHandler implements GameHandler {

    @Override
    public GameCategory getCategory() {
        return GameCategory.MANITTO;
    }

    @Override
    public GameResult handle() {
        log.info("ManittoGameHandler");
        return new GameResult();
    }
}
