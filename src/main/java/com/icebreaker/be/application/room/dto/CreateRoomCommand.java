package com.icebreaker.be.application.room.dto;

import com.icebreaker.be.domain.room.Room;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record CreateRoomCommand(
        @NotBlank
        @Size(max = 255)
        @Schema(description = "방 이름", example = "카카오테크캠퍼스 아이디어톤", maxLength = 255)
        String name,

        @NotNull
        @Min(2)
        @Max(20)
        @Schema(description = "최대 참여 인원", example = "7", minimum = "2", maximum = "20")
        Integer maxParticipants
) {

    public Room toEntity() {
        return Room.builder()
                .name(name)
                .maxParticipants(maxParticipants)
                .build();
    }
}
