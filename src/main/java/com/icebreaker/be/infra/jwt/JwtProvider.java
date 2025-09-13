package com.icebreaker.be.infra.jwt;

import com.icebreaker.be.application.jwt.JwtClaims;
import com.icebreaker.be.application.jwt.JwtService;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider implements JwtService {

    private final Key key;
    private final Long expirationTime;

    public JwtProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration-time}") long expirationTime
    ) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTime = expirationTime;
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();
        Instant expiryTime = now.plusMillis(expirationTime);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public <T extends JwtClaims> String generateTokenWithClaims(String subject, T claims) {
        Instant now = Instant.now();
        Instant expiryTime = now.plusMillis(expirationTime);

        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims.toMap())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        return getClaimsFromToken(token)
                .getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_JWT_TOKEN);
        } catch (SignatureException e) {
            throw new BusinessException(ErrorCode.INVALID_JWT_SIGNATURE);
        } catch (IllegalArgumentException | JwtException e) {
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }
}
