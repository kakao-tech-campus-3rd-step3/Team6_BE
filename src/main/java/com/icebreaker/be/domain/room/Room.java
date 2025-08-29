package com.icebreaker.be.domain.room;

import jakarta.persistence.*;
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

    @Column(name = "room_name", length = 255, nullable = false)
    private String name;

    @Column(name = "room_max_participants", nullable = false)
    private Integer maxParticipants;

    @Builder
    public Room(String name, Integer maxParticipants) {
        this.name = name;
        this.maxParticipants = maxParticipants;
    }
}
