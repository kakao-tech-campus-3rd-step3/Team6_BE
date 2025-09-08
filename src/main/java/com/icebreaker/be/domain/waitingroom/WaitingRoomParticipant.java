package com.icebreaker.be.domain.waitingroom;

import java.time.LocalDateTime;

public record WaitingRoomParticipant(Long userId, String userName, LocalDateTime joinedAt) {

}
