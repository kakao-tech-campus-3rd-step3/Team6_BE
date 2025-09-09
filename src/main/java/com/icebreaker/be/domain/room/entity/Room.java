package com.icebreaker.be.domain.room.entity;

import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_code", nullable = false)
    private String code;

    @Column(name = "room_name", nullable = false)
    private String name;

    @Column(name = "room_capacity", nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomParticipant> roomParticipants = new ArrayList<>();

    @Builder
    public Room(String code, String name, int capacity) {
        this.code = code;
        this.name = name;
        this.capacity = capacity;
    }

    public void joinUsers(List<User> users) {
        List<RoomParticipant> newParticipants = users.stream()
                .map(user -> RoomParticipant.from(this, user))
                .toList();
        joinParticipants(newParticipants);
    }

    public void joinParticipants(List<RoomParticipant> roomParticipants) {
        if (this.roomParticipants.size() + roomParticipants.size() > capacity) {
            throw new BusinessException(ErrorCode.ROOM_CAPACITY_EXCEEDED);
        }
        this.roomParticipants.addAll(roomParticipants);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", roomParticipants=" + roomParticipants +
                '}';
    }
}
