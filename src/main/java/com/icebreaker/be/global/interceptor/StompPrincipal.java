package com.icebreaker.be.global.interceptor;

import java.security.Principal;

public record StompPrincipal(String name) implements Principal {

    @Override
    public String getName() {
        return name;
    }
}
