package com.icebreaker.be.domain.room.repo;

import com.icebreaker.be.domain.room.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByCode(String code);
}
