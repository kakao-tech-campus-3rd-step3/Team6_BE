package com.icebreaker.be.domain.room.entity;

import com.icebreaker.be.domain.room.vo.RoomParticipantRole;
import com.icebreaker.be.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private RoomParticipantRole role = RoomParticipantRole.GUEST;

    @Builder
    public RoomParticipant(Room room, User user) {
        this.room = room;
        this.user = user;
    }

    public static RoomParticipant from(Room room, User user) {
        return RoomParticipant.builder()
                .room(room)
                .user(user)
                .build();
    }

}
