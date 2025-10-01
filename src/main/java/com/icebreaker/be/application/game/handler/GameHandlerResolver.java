package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.global.common.resolver.Resolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameHandlerResolver implements Resolver<GameCategory, GameHandler> {

    private final GameHandlerRegistry registry;

    @Override
    public boolean supports(GameCategory category) {
        return registry.contains(category);
    }

    @Override
    public GameHandler resolve(GameCategory category) {
        return registry.getHandler(category);
    }
}
