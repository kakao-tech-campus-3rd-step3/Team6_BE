package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.dto.GameContext;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.global.common.util.CollectorsUtils;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GameHandlerRegistry {

    private final Map<GameCategory, GameHandler<? extends GameContext>> handlerMap;

    public GameHandlerRegistry(List<GameHandler<? extends GameContext>> actions) {
        this.handlerMap = actions.stream()
                .collect(CollectorsUtils.toMapByKey(GameHandler::getCategory));
    }

    public GameHandler<? extends GameContext> getHandler(GameCategory category) {
        GameHandler<? extends GameContext> handler = handlerMap.get(category);
        if (handler == null) {
            throw new BusinessException(ErrorCode.INVALID_GAME_CATEGORY);
        }
        return handler;
    }
}
