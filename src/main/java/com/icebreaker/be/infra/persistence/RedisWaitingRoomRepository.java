package com.icebreaker.be.infra.persistence;

import com.icebreaker.be.domain.room.WaitingRoomRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public class RedisWaitingRoomRepository implements WaitingRoomRepositoryCustom {
    //TODO: 추후 LUA 관련 코드 작성 예정
}
