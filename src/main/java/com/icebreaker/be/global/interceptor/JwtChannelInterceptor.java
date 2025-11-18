package com.icebreaker.be.global.interceptor;

import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import com.icebreaker.be.infra.jwt.JwtProvider;
import jakarta.annotation.Nullable;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID_ATTRIBUTE = "userId";

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(@Nullable Message<?> message, @Nullable MessageChannel channel) {
        if (message == null) {
            return null;
        }

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
                StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        switch (accessor.getCommand()) {
            case CONNECT -> handleConnect(accessor);
            case SUBSCRIBE, SEND -> restorePrincipal(accessor);
            case DISCONNECT -> handleDisconnect(accessor);
            default -> {
                // ignore
            }
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        String userId = jwtProvider.getSubjectFromToken(token);

        log.info("[STOMP CONNECT] userId={} connected, sessionId={}", userId,
                accessor.getSessionId());

        StompPrincipal principal = new StompPrincipal(userId);
        accessor.setUser(principal);

        Objects.requireNonNull(accessor.getSessionAttributes()).put(USER_ID_ATTRIBUTE, userId);
    }

    private void restorePrincipal(StompHeaderAccessor accessor) {
        if (accessor.getUser() != null) {
            return;
        }

        var sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes == null) {
            return;
        }

        Object userIdObj = sessionAttributes.get(USER_ID_ATTRIBUTE);
        if (userIdObj == null) {
            return;
        }

        String userId = userIdObj.toString();
        accessor.setUser(new StompPrincipal(userId));

        log.debug("[STOMP RESTORE] Restored Principal for userId={} (sessionId={})",
                userId, accessor.getSessionId());
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        var user = accessor.getUser();
        if (user != null) {
            log.info("[STOMP DISCONNECT] userId={} disconnected, sessionId={}", user.getName(),
                    accessor.getSessionId());
        }
    }
}
