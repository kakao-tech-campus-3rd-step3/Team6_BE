package com.icebreaker.be.global.resolver;

import com.icebreaker.be.global.annotation.CurrentUser;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import jakarta.annotation.Nullable;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String USER_ID_ATTRIBUTE = "userId";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
            @Nullable Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getUser() != null) {
            return Long.parseLong(accessor.getUser().getName());
        }

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes != null && sessionAttributes.get(USER_ID_ATTRIBUTE) != null) {
            return Long.parseLong(sessionAttributes.get(USER_ID_ATTRIBUTE).toString());
        }

        throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
    }
}
