package com.icebreaker.be.application.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateUserCommand(
        @NotBlank
        @Size(max = 20)
        @Schema(description = "사용자 이름", example = "홍길동", maxLength = 20)
        String name,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}", message = "전화번호 형식이 올바르지 않습니다")
        @Schema(description = "전화번호", example = "01012345678", pattern = "\\d{10,11}")
        String phone,

        @NotNull
        @Min(0)
        @Max(120)
        @Schema(description = "사용자 나이", example = "25", minimum = "0", maximum = "120")
        Integer age,

        @NotEmpty
        @Schema(description = "관심사", example = "[\"음악\", \"영화\"]")
        Set<@NotBlank String> interests,

        @NotBlank
        @Schema(description = "MBTI 값", example = "INTJ")
        String mbtiValue,

        @NotBlank
        @Size(max = 100)
        @Schema(description = "한줄 소개", example = "안녕하세요, 백엔드 개발자입니다.", maxLength = 100)
        String introduction
) {

}