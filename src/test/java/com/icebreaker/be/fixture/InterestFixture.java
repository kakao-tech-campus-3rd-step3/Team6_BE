package com.icebreaker.be.fixture;

import com.icebreaker.be.domain.interest.Interest;

public class InterestFixture {

    public static final String DEFAULT_INTEREST_NAME_1 = "운동";
    public static final String DEFAULT_INTEREST_NAME_2 = "독서";
    public static final String DEFAULT_INTEREST_NAME_3 = "영화";

    public static Interest interest(String name) {
        return Interest.builder()
                .name(name)
                .build();
    }

    public static Interest defaultInterest1() {
        return interest(DEFAULT_INTEREST_NAME_1);
    }

    public static Interest defaultInterest2() {
        return interest(DEFAULT_INTEREST_NAME_2);
    }

    public static Interest defaultInterest3() {
        return interest(DEFAULT_INTEREST_NAME_3);
    }
}
