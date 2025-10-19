package com.icebreaker.be.domain.topic;

import java.util.List;

public interface TopicRepository {

    void save(String roomCode, Long userId, List<Topic> topic);

    List<Topic> findByRoomAndUser(String roomCode, Long userId);

    List<Topic> findAllByRoom(String roomCode);

    boolean isInitialized(String roomCode);
}
