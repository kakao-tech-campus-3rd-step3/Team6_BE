package com.icebreaker.be.domain.room.vo;

import java.time.LocalDateTime;

public record WaitingRoomParticipant(Long userId, String userName, LocalDateTime joinedAt) {

}
