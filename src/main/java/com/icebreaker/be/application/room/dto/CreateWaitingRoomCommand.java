package com.icebreaker.be.application.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateWaitingRoomCommand(
        @NotBlank
        @Size(max = 255)
        @Schema(description = "방 이름", example = "카카오테크캠퍼스 아이디어톤", maxLength = 255)
        String name,

        @NotNull
        @Min(2)
        @Max(20)
        @Schema(description = "최대 참여 인원", example = "7", minimum = "2", maximum = "20")
        Integer capacity
) {

}
