package com.icebreaker.be.application.room.event;

public record WaitingRoomCreatedEvent(String roomId, Long creatorId) {

}
