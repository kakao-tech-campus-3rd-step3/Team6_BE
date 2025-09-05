package com.icebreaker.be.domain.room.vo;

import java.io.Serializable;

public record WaitingRoom(String roomId, String name, int capacity) implements Serializable {

}