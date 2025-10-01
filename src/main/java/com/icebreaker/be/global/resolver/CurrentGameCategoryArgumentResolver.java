package com.icebreaker.be.global.resolver;

import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.domain.room.repo.RoomOwnerRepository;
import com.icebreaker.be.domain.room.repo.RoomStageRepository;
import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import com.icebreaker.be.presentation.annotation.CurrentGameCategory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrentGameCategoryArgumentResolver implements HandlerMethodArgumentResolver {

    private final RoomStageRepository roomStageRepository;
    private final RoomOwnerRepository roomOwnerRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentGameCategory.class)
                && parameter.getParameterType().equals(GameCategory.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        CurrentGameCategory annotation = parameter.getParameterAnnotation(
                CurrentGameCategory.class);
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String roomCode = getRoomCode(accessor, annotation.roomCodeParam());
        Long userId = getUserId(accessor);

        checkRoomOwner(roomCode, userId);
        Stage currentStage = getCurrentStage(roomCode);

        GameCategory category;
        try {
            category = GameCategory.fromStage(currentStage);
        } catch (IllegalArgumentException e) {
            log.warn("RoomStage가 게임이 아님: roomCode={}, stage={}", roomCode, currentStage);
            throw new BusinessException(ErrorCode.INVALID_ROOM_STAGE);
        }

        log.debug("GameCategory argument resolved ✅ roomCode={}, userId={}, category={}", roomCode,
                userId, category);
        return category;
    }

    private Long getUserId(StompHeaderAccessor accessor) {
        if (accessor.getUser() != null) {
            try {
                return Long.parseLong(accessor.getUser().getName());
            } catch (NumberFormatException e) {
                throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
            }
        }
        throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
    }

    private String getRoomCode(StompHeaderAccessor accessor, String roomCodeParam) {
        Object roomCodeObj = Objects.requireNonNull(accessor.getSessionAttributes())
                .get(roomCodeParam);
        if (roomCodeObj instanceof String roomCodeStr && !roomCodeStr.isBlank()) {
            return roomCodeStr;
        }
        throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
    }

    private void checkRoomOwner(String roomCode, Long userId) {
        Long ownerId = roomOwnerRepository.findOwnerByRoomCode(roomCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_OWNER_NOT_FOUND));

        if (!ownerId.equals(userId)) {
            log.warn("방장 불일치: roomCode={}, userId={}, ownerId={}", roomCode, userId, ownerId);
            throw new BusinessException(ErrorCode.ROOM_OWNER_MISMATCH);
        }
    }

    private Stage getCurrentStage(String roomCode) {
        RoomStage roomStage = roomStageRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
        return roomStage.stage();
    }
}

