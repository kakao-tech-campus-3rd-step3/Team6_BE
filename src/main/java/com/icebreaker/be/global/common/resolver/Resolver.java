package com.icebreaker.be.global.common.resolver;

public interface Resolver<K, V> {

    boolean supports(K key);

    V resolve(K key);
}