package com.icebreaker.be.application.user.dto;

public record UserIdWithTokenResponse(
        Long userId,
        String token
) {

}
