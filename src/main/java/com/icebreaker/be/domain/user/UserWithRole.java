package com.icebreaker.be.domain.user;

public record UserWithRole(
        User user,
        String role
) {

}
