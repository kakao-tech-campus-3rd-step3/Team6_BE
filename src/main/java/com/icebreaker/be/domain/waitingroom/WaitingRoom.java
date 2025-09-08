package com.icebreaker.be.domain.waitingroom;

import java.io.Serializable;

public record WaitingRoom(String roomId, String name, int capacity) implements
        Serializable {

}