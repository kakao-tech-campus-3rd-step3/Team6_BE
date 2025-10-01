package com.icebreaker.be.application.room.dto;

import com.icebreaker.be.domain.user.User;
import java.util.Set;
import java.util.stream.Collectors;

public record RoomParticipantCommand(
        Long id,
        String name,
        Integer age,
        Set<String> interests
) {

    public static RoomParticipantCommand fromEntity(User user) {
        return new RoomParticipantCommand(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getInterestsEnum().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );
    }
}
