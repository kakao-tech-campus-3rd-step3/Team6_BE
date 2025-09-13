package com.icebreaker.be.application.jwt;

import java.util.Map;

public interface JwtClaims {

    Map<String, Object> toMap();
}
