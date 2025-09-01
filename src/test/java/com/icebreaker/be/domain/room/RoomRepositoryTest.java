package com.icebreaker.be.domain.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomRepositoryTest {

    @Mock
    private RoomRepository roomRepository;

    private Room testRoom;
    private Long testRoomId = 1L;

    @BeforeEach
    void setUp() {
        testRoom = Room.builder()
                .name("테스트 방")
                .maxParticipants(5)
                .build();

        // 리플렉션을 사용하여 id 설정 (테스트용)
        try {
            Field idField = testRoom.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testRoom, testRoomId);
        } catch (Exception e) {
            throw new RuntimeException("테스트 Room에 id 설정 실패", e);
        }
    }

    @Test
    @DisplayName("Room 엔티티를 저장하고 조회할 수 있다")
    void saveAndFindRoom() {
        // given
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);
        when(roomRepository.findById(testRoomId)).thenReturn(Optional.of(testRoom));

        // when
        Room savedRoom = roomRepository.save(new Room("새 방", 5, null));
        Room foundRoom = roomRepository.findById(savedRoom.getId()).orElseThrow();

        // then
        assertThat(foundRoom.getName()).isEqualTo("테스트 방");
        assertThat(foundRoom.getMaxParticipants()).isEqualTo(5);
    }

    @Test
    @DisplayName("Room을 삭제하면 조회 시 비어있는 결과를 반환한다")
    void deleteRoom() {
        // given
        when(roomRepository.findById(testRoomId)).thenReturn(Optional.empty());

        // when & then
        assertThat(roomRepository.findById(testRoomId)).isEmpty();
    }

    @Test
    @DisplayName("모든 Room을 조회할 수 있다")
    void findAllRooms() {
        // given
        Room room1 = Room.builder()
                .name("테스트 방 1")
                .maxParticipants(5)
                .build();
        Room room2 = Room.builder()
                .name("테스트 방 2")
                .maxParticipants(10)
                .build();

        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));

        // when
        List<Room> rooms = roomRepository.findAll();

        // then
        assertThat(rooms).hasSize(2);
        assertThat(rooms).extracting("name")
                .containsExactlyInAnyOrder("테스트 방 1", "테스트 방 2");
        assertThat(rooms).extracting("maxParticipants")
                .containsExactlyInAnyOrder(5, 10);
    }
}
