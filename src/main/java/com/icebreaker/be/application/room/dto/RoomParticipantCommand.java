package com.icebreaker.be.application.room.dto;

import com.icebreaker.be.domain.user.Interest;
import com.icebreaker.be.domain.user.MbtiType;
import com.icebreaker.be.domain.user.User;
import java.util.Set;

public record RoomParticipantCommand(
        Long id,
        String name,
        Integer age,
        MbtiType mbtiType,
        Set<Interest> interests
) {

    public static RoomParticipantCommand fromEntity(User user) {
        return new RoomParticipantCommand(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getMbti(),
                user.getInterestsEnum()
        );
    }
}
