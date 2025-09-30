package com.icebreaker.be.domain.room.repo;

import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByCode(String code);

    Optional<Room> findByCode(String code);

    @Query("SELECT rp.user FROM RoomParticipant rp WHERE rp.room.code = :roomCode")
    List<User> findUsersByRoomCode(@Param("roomCode") String roomCode);
}
