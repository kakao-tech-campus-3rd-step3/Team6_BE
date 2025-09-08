package com.icebreaker.be.global.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Component
@ConfigurationProperties(prefix = "websocket.heartbeat")
public class WebSocketHeartbeatProperties {

    private long serverInterval;
    private long clientInterval;

    public long[] getHeartbeat() {
        return new long[]{serverInterval, clientInterval};
    }
}