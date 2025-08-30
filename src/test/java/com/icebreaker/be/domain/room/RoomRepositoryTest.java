package com.icebreaker.be.domain.room;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("RoomRepository 테스트")
public class RoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @DisplayName("id로 방을 조회")
    void findByIdTest() {
        Room room = new Room("room1", 7);
        Room savedRoom = entityManager.persistAndFlush(room);
        Optional<Room> actual = roomRepository.findById(savedRoom.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(savedRoom.getName());
        assertThat(actual.get().getMaxParticipants()).isEqualTo(savedRoom.getMaxParticipants());
    }

    @Test
    @DisplayName("존재하지 않는 id로 조회 시 오류")
    void findByIdNotFoundTest() {
        Optional<Room> actual = roomRepository.findById(100L);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("방 생성")
    void saveTest() {
        Room room = new Room("room1", 7);
        Room savedRoom = roomRepository.save(room);

        assertThat(savedRoom.getId()).isNotNull();
        assertThat(savedRoom.getName()).isEqualTo(room.getName());
        assertThat(savedRoom.getMaxParticipants()).isEqualTo(room.getMaxParticipants());
    }

    @Test
    @DisplayName("방 삭제")
    void deleteTest() {
        Room room = new Room("room1", 7);
        Room savedRoom = entityManager.persistAndFlush(room);
        roomRepository.delete(room);

        Optional<Room> actual = roomRepository.findById(savedRoom.getId());
        assertThat(actual).isEmpty();
    }
}
