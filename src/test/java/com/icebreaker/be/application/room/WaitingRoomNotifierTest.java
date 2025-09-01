package com.icebreaker.be.application.room;

import com.icebreaker.be.domain.room.Participant;
import com.icebreaker.be.domain.room.Room;
import com.icebreaker.be.domain.room.WaitingRoom;
import com.icebreaker.be.domain.room.WaitingRoomStatus;
import com.icebreaker.be.domain.user.MbtiType;
import com.icebreaker.be.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WaitingRoomNotifierTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WaitingRoomNotifier waitingRoomNotifier;

    private WaitingRoom waitingRoom;
    private Room room;

    @BeforeEach
    void setUp() {
        // 대기실 설정
        waitingRoom = new WaitingRoom("test-room-id", "테스트 방", 3);
        waitingRoom.join(1L);
        waitingRoom.join(2L);

        // 방 설정
        User user1 = User.builder()
                .name("유저1")
                .phone("010-1111-1111")
                .age(20)
                .mbti(MbtiType.ENFJ)
                .introduction("소개1")
                .build();
        User user2 = User.builder()
                .name("유저2")
                .phone("010-2222-2222")
                .age(25)
                .mbti(MbtiType.INTJ)
                .introduction("소개2")
                .build();

        List<Participant> participants = new ArrayList<>();
        participants.add(Participant.builder().user(user1).build());
        participants.add(Participant.builder().user(user2).build());

        room = Room.builder()
                .name("테스트 방")
                .maxParticipants(2)
                .participants(participants)
                .build();
    }

    @Test
    @DisplayName("대기실 상태 변경 알림을 보낸다")
    void notifyStatus() {
        // given
        WaitingRoomStatus status = WaitingRoomStatus.WAITING;

        // when
        waitingRoomNotifier.notifyStatus(waitingRoom, status);

        // then
        verify(messagingTemplate).convertAndSend(
                eq("/topic/waiting-room/" + waitingRoom.getRoomId()),
                anyMap()
        );
    }

    @Test
    @DisplayName("방 시작 알림을 보낸다")
    void notifyRoomStart() {
        // given & when
        waitingRoomNotifier.notifyRoomStart(room);

        // then
        verify(messagingTemplate).convertAndSend(
                eq("/topic/room-start/" + room.getId()),
                anyMap()
        );
    }
}
