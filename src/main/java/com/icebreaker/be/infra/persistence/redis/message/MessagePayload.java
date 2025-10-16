package com.icebreaker.be.infra.persistence.redis.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @Type(value = RoomStageChangeMessage.class, name = "ROOM_STAGE_CHANGE"),
        @Type(value = ParticipantJoinedMessage.class, name = "PARTICIPANT_JOINED"),
        @Type(value = RoomStartedMessage.class, name = "ROOM_STARTED")
})
public interface MessagePayload {

}
