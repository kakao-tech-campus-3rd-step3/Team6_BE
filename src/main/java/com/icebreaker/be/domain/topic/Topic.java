package com.icebreaker.be.domain.topic;

/**
 * 사용자에게서 추출하여 관심사에 대한 토픽을 나타내는 도메인 객체
 */
public record Topic(String name) {

    public static Topic of(String name) {
        return new Topic(name);
    }
}
