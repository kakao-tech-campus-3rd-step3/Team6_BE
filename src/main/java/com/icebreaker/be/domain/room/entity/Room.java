package com.icebreaker.be.domain.room.entity;

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

    @Column(name = "room_max_participants", nullable = false)
    private Integer maxParticipants;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomParticipant> roomParticipants = new ArrayList<>();

    @Builder
    public Room(String code, String name, int maxParticipants) {
        this.code = code;
        this.name = name;
        this.maxParticipants = maxParticipants;
    }

    public void joinParticipants(List<RoomParticipant> roomParticipants) {
        this.roomParticipants = roomParticipants;
    }
}
