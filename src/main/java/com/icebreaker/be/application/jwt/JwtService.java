package com.icebreaker.be.application.jwt;

public interface JwtService {

    String generateToken(String subject);

    <T extends JwtClaims> String generateTokenWithClaims(
            String subject,
            T claims
    );

    String getSubjectFromToken(String token);
}
