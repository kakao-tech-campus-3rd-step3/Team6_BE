package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.dto.GameContext;
import com.icebreaker.be.application.game.dto.GameResult;
import com.icebreaker.be.domain.game.GameCategory;

public interface GameHandler<C extends GameContext> {

    GameCategory getCategory();

    GameResult handle(C context);
}
