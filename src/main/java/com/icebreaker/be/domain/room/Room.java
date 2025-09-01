package com.icebreaker.be.domain.room;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    @Builder
    public Room(String name, Integer maxParticipants, List<Participant> participants) {
        this.name = name;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
    }

    public void joinParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
