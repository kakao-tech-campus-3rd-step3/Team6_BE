package com.icebreaker.be.application.game.util;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MatchingUtils {

    /**
     * O(n) Derangement 기반 1:1 랜덤 매칭 자기 자신 배정 없고, 모든 참가자 1:1 매칭 보장
     */
    public static <T> Map<T, T> generateDerangementPairs(List<T> participants) {
        int n = participants.size();
        if (n < 2) {
            throw new IllegalArgumentException("참가자는 최소 2명 이상이어야 합니다.");
        }

        List<T> receivers = new ArrayList<>(participants);
        Random rand = new Random();

        // O(n) Fisher-Yates 기반 derangement
        for (int i = 0; i < n; i++) {
            int j = i + rand.nextInt(n - i);
            // swap
            T temp = receivers.get(i);
            receivers.set(i, receivers.get(j));
            receivers.set(j, temp);

            // 자기 자신이면 마지막 요소와 swap
            if (receivers.get(i).equals(participants.get(i))) {
                int swapIndex = (i == n - 1) ? i - 1 : n - 1;
                T t = receivers.get(i);
                receivers.set(i, receivers.get(swapIndex));
                receivers.set(swapIndex, t);
            }
        }

        Map<T, T> pairs = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            pairs.put(participants.get(i), receivers.get(i));
        }

        return pairs;
    }
}
