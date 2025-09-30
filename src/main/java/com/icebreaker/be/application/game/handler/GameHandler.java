package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.GameResult;
import com.icebreaker.be.domain.game.GameCategory;

public interface GameHandler {

    GameCategory getCategory();

    GameResult handle();
}
