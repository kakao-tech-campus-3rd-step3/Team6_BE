package com.icebreaker.be.domain.room.vo;

import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.domain.user.Interest;
import java.util.List;
import java.util.Set;

public record RoomParticipantInterest(
        Long id,
        Set<Interest> interests
) {

    public List<Topic> toTopics() {
        return interests.stream()
                .map(Interest::toTopic)
                .toList();
    }
}