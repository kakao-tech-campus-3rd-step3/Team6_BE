package com.icebreaker.be.application.game.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class MatchingUtils {

    /**
     * O(n) Derangement 기반 1:1 랜덤 매칭 자기 자신 배정 없고, 모든 참가자 1:1 매칭 보장
     */
    public static <T> Map<T, T> generateDerangementPairs(List<T> participants) {
        Objects.requireNonNull(participants, "participants");
        int n = participants.size();
        if (n < 2) {
            throw new IllegalArgumentException("참가자는 최소 2명 이상이어야 합니다.");
        }
        List<T> receivers = new ArrayList<>(participants);
        int shift = 1 + ThreadLocalRandom.current().nextInt(n - 1); // 1..n-1
        Collections.rotate(receivers, shift);

        Map<T, T> pairs = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            pairs.put(participants.get(i), receivers.get(i));
        }

        return pairs;
    }
}
