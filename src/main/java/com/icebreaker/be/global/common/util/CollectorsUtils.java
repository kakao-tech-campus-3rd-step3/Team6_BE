package com.icebreaker.be.global.common.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CollectorsUtils {

    /**
     * keyMapper만 넘기면 value는 자기 자신으로 설정, 불변 Map 생성
     */
    public static <T, K> Collector<T, ?, Map<K, T>> toMapByKey(Function<T, K> keyMapper) {
        return Collectors.toUnmodifiableMap(keyMapper, t -> t);
    }
}