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
        if (!supports(category)) {
            throw new IllegalArgumentException("해당 category 대한 Action이 없습니다: " + category);
        }
        return registry.getHandler(category);
    }
}
