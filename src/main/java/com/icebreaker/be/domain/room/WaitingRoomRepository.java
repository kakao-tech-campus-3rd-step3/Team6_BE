package com.icebreaker.be.domain.room;

import org.springframework.data.repository.CrudRepository;

public interface WaitingRoomRepository extends CrudRepository<WaitingRoom, String>, WaitingRoomRepositoryCustom {

}
