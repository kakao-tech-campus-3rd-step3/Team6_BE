package com.icebreaker.be.global.interceptor;

import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import com.icebreaker.be.infra.jwt.JwtProvider;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtProvider jwtProvider;

    @Override
    protected Principal determineUser(ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders == null || authHeaders.isEmpty()) {
            log.warn("[HANDSHAKE] Authorization header missing");
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }

        String header = authHeaders.get(0);
        if (!header.startsWith("Bearer ")) {
            log.warn("[HANDSHAKE] Invalid token format: {}", header);
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }

        String token = header.substring(7);
        String userId = jwtProvider.getSubjectFromToken(token);

        log.info("[HANDSHAKE] Principal registered: userId={}", userId);
        return new StompPrincipal(userId);
    }
}
