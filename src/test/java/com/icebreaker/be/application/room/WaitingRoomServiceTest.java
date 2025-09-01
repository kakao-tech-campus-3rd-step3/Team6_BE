package com.icebreaker.be.application.room;

import com.icebreaker.be.application.room.dto.CreateRoomCommand;
import com.icebreaker.be.domain.room.*;
import com.icebreaker.be.domain.user.MbtiType;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.global.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingRoomServiceTest {

    private final String testRoomId = "테스트 방";
    @Mock
    private WaitingRoomRepository waitingRoomRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WaitingRoomIdGenerator idGenerator;
    @Mock
    private WaitingRoomNotifier notifier;
    @Mock
    private WaitingRoomEventPublisher eventPublisher;
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
        CreateRoomCommand command = new CreateRoomCommand("테스트 방", 3);

        when(idGenerator.generate()).thenReturn(testRoomId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(waitingRoomRepository.findById(testRoomId)).thenReturn(Optional.of(testWaitingRoom));

        // when
        String roomId = waitingRoomService.createRoom(command, userId);

        // then
        assertThat(roomId).isEqualTo(testRoomId);
        // createRoom 메서드에서 한 번, joinRoom 메서드에서 한 번, 총 두 번 호출됨
        verify(waitingRoomRepository, times(2)).save(any(WaitingRoom.class));
        verify(notifier).notifyStatus(any(WaitingRoom.class), any(WaitingRoomStatus.class));
    }

    @Test
    @DisplayName("사용자가 대기실에 참가한다")
    void joinRoomWithValidRoomIdAndUserId() {
        // given
        Long userId = 1L;

        when(waitingRoomRepository.findById(testRoomId)).thenReturn(Optional.of(testWaitingRoom));
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // when
        waitingRoomService.joinRoom(testRoomId, userId);

        // then
        verify(waitingRoomRepository).save(testWaitingRoom);
        verify(notifier).notifyStatus(any(WaitingRoom.class), any(WaitingRoomStatus.class));
    }

    @Test
    @DisplayName("존재하지 않는 대기실에 참가하려고 하면 예외가 발생한다")
    void joinNonExistingRoomThrowsException() {
        // given
        Long userId = 1L;
        String nonExistingRoomId = "non-existing-room";

        when(waitingRoomRepository.findById(nonExistingRoomId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> waitingRoomService.joinRoom(nonExistingRoomId, userId))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 대기실에 참가하려고 하면 예외가 발생한다")
    void joinRoomWithNonExistingUserThrowsException() {
        // given
        Long nonExistingUserId = 999L;

        when(waitingRoomRepository.findById(testRoomId)).thenReturn(Optional.of(testWaitingRoom));
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
        WaitingRoom fullRoom = new WaitingRoom(testRoomId, "테스트 방", 1);

        when(waitingRoomRepository.findById(testRoomId)).thenReturn(Optional.of(fullRoom));
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        doNothing().when(eventPublisher).publishRoomFullEvent(anyString());

        // when
        waitingRoomService.joinRoom(testRoomId, userId);

        // then
        verify(eventPublisher).publishRoomFullEvent(testRoomId);
    }

    @Test
    @DisplayName("WaitingRoomFullEvent가 발생하면 실제 Room을 생성하고 대기실을 삭제한다")
    void handleWaitingRoomFullEventCreatesRoomAndDeletesWaitingRoom() {
        // given
        WaitingRoomFullEvent event = new WaitingRoomFullEvent(testRoomId);
        Set<Long> participants = new HashSet<>();
        participants.add(1L);
        participants.add(2L);

        // WaitingRoom 모킹
        WaitingRoom waitingRoom = new WaitingRoom(testRoomId, "테스트 방", 2);
        participants.forEach(waitingRoom::join);

        when(waitingRoomRepository.findById(testRoomId)).thenReturn(Optional.of(waitingRoom));

        // 참가자 User 객체 모킹
        List<User> users = new ArrayList<>();
        users.add(User.builder().name("유저1").phone("010-1111-1111").age(20).mbti(MbtiType.ENFJ).introduction("소개1").build());
        users.add(User.builder().name("유저2").phone("010-2222-2222").age(25).mbti(MbtiType.INTJ).introduction("소개2").build());

        when(userRepository.findAllById(participants)).thenReturn(users);

        // Room 저장 모킹
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.<Room>getArgument(0));

        // when
        waitingRoomService.handleWaitingRoomFullEvent(event);

        // then
        // Room 저장 검증
        ArgumentCaptor<Room> roomCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomCaptor.capture());
        Room savedRoom = roomCaptor.getValue();

        assertThat(savedRoom.getName()).isEqualTo(testRoomId);
        assertThat(savedRoom.getMaxParticipants()).isEqualTo(users.size());

        // WaitingRoom 삭제 검증
        verify(waitingRoomRepository).delete(testRoomId);

        // 방 시작 알림 검증
        verify(notifier).notifyRoomStart(any(Room.class));
    }
}
