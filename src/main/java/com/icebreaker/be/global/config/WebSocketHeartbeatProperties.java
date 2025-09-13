package com.icebreaker.be.global.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Component
@ConfigurationProperties(prefix = "websocket.heartbeat")
class WebSocketHeartbeatProperties {

    private long serverInterval = 10_000;
    private long clientInterval = 10_000;

    public long[] getHeartbeat() {
        return new long[]{serverInterval, clientInterval};
    }
}