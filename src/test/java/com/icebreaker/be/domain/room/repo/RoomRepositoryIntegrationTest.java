package com.icebreaker.be.domain.room.repo;

import static org.assertj.core.api.Assertions.assertThat;

import com.icebreaker.be.config.TestLlmConfig;
import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.room.entity.RoomParticipant;
import com.icebreaker.be.domain.user.MbtiType;
import com.icebreaker.be.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import(TestLlmConfig.class)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb"
})
@DisplayName("RoomRepository 통합 테스트")
class RoomRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @DisplayName("방을 저장하고 조회할 수 있다")
    void saveAndFind_Success() {
        // given
        Room room = Room.builder()
                .code("TEST-ROOM")
                .name("테스트 방")
                .capacity(4)
                .build();

        // when
        Room savedRoom = roomRepository.save(room);
        Optional<Room> foundRoom = roomRepository.findById(savedRoom.getId());

        // then
        assertThat(foundRoom).isPresent();
        assertThat(foundRoom.get().getCode()).isEqualTo("TEST-ROOM");
        assertThat(foundRoom.get().getName()).isEqualTo("테스트 방");
        assertThat(foundRoom.get().getCapacity()).isEqualTo(4);
    }

    @Test
    @DisplayName("방과 참여자들을 함께 저장할 수 있다")
    void saveRoomWithParticipants_Success() {
        // given
        User user1 = User.builder()
                .name("사용자1")
                .phone("010-1111-1111")
                .age(25)
                .mbti(MbtiType.ENFP)
                .introduction("첫 번째 사용자")
                .build();

        User user2 = User.builder()
                .name("사용자2")
                .phone("010-2222-2222")
                .age(30)
                .mbti(MbtiType.INTJ)
                .introduction("두 번째 사용자")
                .build();

        // User들을 저장
        User savedUser1 = entityManager.persistAndFlush(user1);
        User savedUser2 = entityManager.persistAndFlush(user2);

        Room room = Room.builder()
                .code("ROOM-WITH-USERS")
                .name("사용자가 있는 방")
                .capacity(5)
                .build();

        // Room을 저장
        Room savedRoom = entityManager.persistAndFlush(room);

        RoomParticipant participant1 = RoomParticipant.builder()
                .room(savedRoom)
                .user(savedUser1)
                .build();

        RoomParticipant participant2 = RoomParticipant.builder()
                .room(savedRoom)
                .user(savedUser2)
                .build();

        // RoomParticipant들을 저장
        entityManager.persistAndFlush(participant1);
        entityManager.persistAndFlush(participant2);

        // 세션을 클리어하고 다시 조회하여 Lazy Loading 확인
        entityManager.clear();

        // when
        Optional<Room> foundRoom = roomRepository.findById(savedRoom.getId());

        // then
        assertThat(foundRoom).isPresent();

        // Lazy Loading을 강제로 초기화
        Room room1 = foundRoom.get();
        int participantCount = room1.getRoomParticipants().size(); // Lazy Loading 트리거

        assertThat(participantCount).isEqualTo(2);

        List<String> participantNames = room1.getRoomParticipants()
                .stream()
                .map(rp -> rp.getUser().getName())
                .toList();

        assertThat(participantNames).containsExactlyInAnyOrder("사용자1", "사용자2");
    }

    @Test
    @DisplayName("모든 방을 조회할 수 있다")
    void findAll_Success() {
        // given
        Room room1 = Room.builder()
                .code("ROOM-001")
                .name("첫 번째 방")
                .capacity(3)
                .build();

        Room room2 = Room.builder()
                .code("ROOM-002")
                .name("두 번째 방")
                .capacity(5)
                .build();

        roomRepository.save(room1);
        roomRepository.save(room2);

        // when
        List<Room> rooms = roomRepository.findAll();

        // then
        assertThat(rooms).hasSize(2);
        assertThat(rooms).extracting(Room::getCode)
                .containsExactlyInAnyOrder("ROOM-001", "ROOM-002");
    }

    @Test
    @DisplayName("방을 삭제하면 연관된 참여자도 함께 삭제된다 (cascade)")
    void deleteRoom_CascadeParticipants() {
        // given
        User user = User.builder()
                .name("테스트 사용자")
                .phone("010-9999-9999")
                .age(25)
                .mbti(MbtiType.ENFP)
                .introduction("테스트")
                .build();

        // User를 저장
        User savedUser = entityManager.persistAndFlush(user);

        Room room = Room.builder()
                .code("DELETE-TEST")
                .name("삭제 테스트 방")
                .capacity(4)
                .build();

        // Room을 저장
        Room savedRoom = entityManager.persistAndFlush(room);

        RoomParticipant participant = RoomParticipant.builder()
                .room(savedRoom)
                .user(savedUser)
                .build();

        // Participant를 저장
        RoomParticipant savedParticipant = entityManager.persistAndFlush(participant);

        Long roomId = savedRoom.getId();
        Long participantId = savedParticipant.getId();

        // 모든 변경사항을 커밋하고 세션 클리어
        entityManager.clear();

        // when - Repository를 통해 Room을 다시 조회한 후 삭제
        Room roomToDelete = roomRepository.findById(roomId).orElseThrow();
        roomRepository.delete(roomToDelete);
        roomRepository.flush(); // Repository의 flush 사용

        // then
        assertThat(roomRepository.findById(roomId)).isEmpty();

        // RoomParticipant도 함께 삭제되었는지 확인
        // 새로운 EntityManager 트랜잭션에서 확인
        entityManager.clear();
        RoomParticipant foundParticipant = entityManager.find(RoomParticipant.class, participantId);
        assertThat(foundParticipant).isNull();
    }
}
