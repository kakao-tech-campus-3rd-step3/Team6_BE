package com.icebreaker.be.application.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.icebreaker.be.application.waitingroom.WaitingRoomService;
import com.icebreaker.be.application.waitingroom.dto.CreateWaitingRoomCommand;
import com.icebreaker.be.application.waitingroom.dto.WaitingRoomId;
import com.icebreaker.be.application.waitingroom.event.WaitingRoomEventPublisher;
import com.icebreaker.be.domain.room.service.WaitingRoomIdGenerator;
import com.icebreaker.be.domain.user.MbtiType;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.domain.waitingroom.WaitingRoomParticipant;
import com.icebreaker.be.domain.waitingroom.WaitingRoomRepository;
import com.icebreaker.be.domain.waitingroom.WaitingRoomStatus;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipantIds;
import com.icebreaker.be.global.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WaitingRoomServiceTest {

    private final String testRoomId = "테스트 방";

    @Mock
    private WaitingRoomRepository waitingRoomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WaitingRoomIdGenerator idGenerator;

    @Mock
    private WaitingRoomEventPublisher waitingRoomEventPublisher;

    @InjectMocks
    private WaitingRoomService waitingRoomService;

    private User testUser;
    private WaitingRoom testWaitingRoom;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("테스트 유저")
                .phone("010-1234-5678")
                .age(25)
                .mbti(MbtiType.ENFJ)
                .introduction("테스트용 유저입니다.")
                .build();

        testWaitingRoom = new WaitingRoom(testRoomId, "테스트 방", 3);
    }

    @Test
    @DisplayName("대기실을 생성하고 방장이 참가하여 roomId를 반환한다")
    void createRoomAndJoinCreator() {
        // given
        Long userId = 1L;
        CreateWaitingRoomCommand command = new CreateWaitingRoomCommand("테스트 방", 3);

        when(idGenerator.generate()).thenReturn(testRoomId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        doNothing().when(waitingRoomRepository)
                .initWaitingRoom(any(WaitingRoom.class), any(WaitingRoomParticipant.class));

        // when
        WaitingRoomId roomId = waitingRoomService.createRoom(command, userId);

        // then
        assertThat(roomId).isEqualTo(WaitingRoomId.of(testRoomId));
        verify(waitingRoomRepository).initWaitingRoom(any(WaitingRoom.class),
                any(WaitingRoomParticipant.class));
    }

    @Test
    @DisplayName("사용자가 대기실에 참가한다")
    void joinRoomWithValidRoomIdAndUserId() {
        // given
        Long userId = 1L;
        WaitingRoomWithParticipantIds mockResult = new WaitingRoomWithParticipantIds(
                WaitingRoomStatus.AVAILABLE,
                testWaitingRoom,
                List.of(userId)
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(waitingRoomRepository.joinWaitingRoom(anyString(), any(WaitingRoomParticipant.class)))
                .thenReturn(mockResult);
        doNothing().when(waitingRoomEventPublisher)
                .publishJoined(anyString(), any(WaitingRoomParticipant.class));

        // when
        waitingRoomService.joinRoom(testRoomId, userId);

        // then
        verify(waitingRoomRepository).joinWaitingRoom(anyString(),
                any(WaitingRoomParticipant.class));
        verify(waitingRoomEventPublisher).publishJoined(anyString(),
                any(WaitingRoomParticipant.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 대기실에 참가하려고 하면 예외가 발생한다")
    void joinRoomWithNonExistingUserThrowsException() {
        // given
        Long nonExistingUserId = 999L;

        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> waitingRoomService.joinRoom(testRoomId, nonExistingUserId))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("대기실이 가득 차면 이벤트를 발행한다")
    void publishEventWhenRoomIsFull() {
        // given
        Long userId = 1L;
        WaitingRoomWithParticipantIds fullRoomResult = new WaitingRoomWithParticipantIds(
                WaitingRoomStatus.FULL,
                testWaitingRoom,
                List.of(userId)
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(waitingRoomRepository.joinWaitingRoom(anyString(), any(WaitingRoomParticipant.class)))
                .thenReturn(fullRoomResult);
        doNothing().when(waitingRoomEventPublisher)
                .publishJoined(anyString(), any(WaitingRoomParticipant.class));
        doNothing().when(waitingRoomEventPublisher)
                .publishFulled(any(WaitingRoomWithParticipantIds.class));

        // when
        waitingRoomService.joinRoom(testRoomId, userId);

        // then
        verify(waitingRoomEventPublisher).publishFulled(any(WaitingRoomWithParticipantIds.class));
    }
}
