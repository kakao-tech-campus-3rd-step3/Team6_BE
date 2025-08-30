package com.icebreaker.be.application.room;

import com.icebreaker.be.application.room.dto.CreateRoomCommand;
import com.icebreaker.be.application.room.dto.RoomResponse;
import com.icebreaker.be.domain.room.Room;
import com.icebreaker.be.domain.room.RoomRepository;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomService 테스트")
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    @DisplayName("id로 방 조회")
    void getRoomByIdTest() {
        Long id = 2L;
        Room savedRoom = new Room("room1", 7);
        ReflectionTestUtils.setField(savedRoom, "id", id);
        when(roomRepository.findById(id)).thenReturn(Optional.of(savedRoom));
        RoomResponse roomResponse = roomService.getRoomById(id);

        assertThat(roomResponse.id()).isEqualTo(savedRoom.getId());
        assertThat(roomResponse.name()).isEqualTo(savedRoom.getName());
        assertThat(roomResponse.maxParticipants()).isEqualTo(savedRoom.getMaxParticipants());
    }

    @Test
    @DisplayName("존재하지 않는 id로 방 조회 시 오류")
    void getRoomByIdNotFoundTest() {
        Long id = 1L;
        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.getRoomById(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ROOM_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("전체 방 조회")
    void getAllRoomsTest() {
        Room savedRoom1 = new Room("room1", 7);
        Room savedRoom2 = new Room("room2", 8);
        ReflectionTestUtils.setField(savedRoom1, "id", 1L);
        ReflectionTestUtils.setField(savedRoom2, "id", 2L);
        when(roomRepository.findAll()).thenReturn((List.of(savedRoom1, savedRoom2)));
        List<RoomResponse> roomResponseList = roomService.getAllRooms();

        assertThat(roomResponseList.size()).isEqualTo(2);
        assertThat(roomResponseList.get(0).name()).isEqualTo(savedRoom1.getName());
        assertThat(roomResponseList.get(0).maxParticipants()).isEqualTo(savedRoom1.getMaxParticipants());
        assertThat(roomResponseList.get(1).name()).isEqualTo(savedRoom2.getName());
        assertThat(roomResponseList.get(1).maxParticipants()).isEqualTo(savedRoom2.getMaxParticipants());
    }

    @Test
    @DisplayName("방 생성")
    void createRoomTest() {
        Long id = 2L;
        Room savedRoom = new Room("room1", 7);
        ReflectionTestUtils.setField(savedRoom, "id", id);
        CreateRoomCommand createRoomCommand = new CreateRoomCommand("room1", 7);
        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);
        Long actual = roomService.createRoom(createRoomCommand);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(savedRoom.getId());
    }

    @Test
    @DisplayName("방 삭제")
    void deleteRoomByIdTest() {
        Long id = 2L;
        Room savedRoom = new Room("room1", 7);
        ReflectionTestUtils.setField(savedRoom, "id", id);
        when(roomRepository.findById(id)).thenReturn(Optional.of(savedRoom));
        roomService.deleteRoomById(id);

        then(roomRepository).should(times(1)).findById(id);
        then(roomRepository).should(times(1)).delete(savedRoom);
    }

    @Test
    @DisplayName("존재하지 않는 id로 방 삭제 시 오류")
    void deleteRoomByIdNotFoundTest() {
        Long id = 10L;
        when(roomRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> roomService.deleteRoomById(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ROOM_NOT_FOUND.getMessage());

        then(roomRepository).should(times(1)).findById(id);
        then(roomRepository).should(never()).delete(any(Room.class));
    }
}
