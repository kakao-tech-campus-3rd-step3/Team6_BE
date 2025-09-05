package com.icebreaker.be.infra.persistence.redis;

public enum EmptyArgs implements RedisArgs {
    INSTANCE;

    @Override
    public Object[] toArray() {
        return new Object[0];
    }
}