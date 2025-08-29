package com.icebreaker.be.domain.user;

import com.icebreaker.be.domain.interest.Interest;
import com.icebreaker.be.domain.interest.InterestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", length = 20, nullable = false)
    private String name;

    @Column(name = "user_phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "user_age", nullable = false)
    private Integer age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Interest> interests = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "user_mbti", length = 4, nullable = false)
    private MbtiType mbti;

    @Column(name = "user_introduction", length = 255, nullable = false)
    private String introduction;

    @Builder
    public User(String name, String phone, Integer age, List<InterestType> interestTypes, MbtiType mbti, String introduction) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.mbti = mbti;
        this.introduction = introduction;

        if (interestTypes != null) {
            this.interests = interestTypes.stream()
                    .map(interestType -> Interest.builder()
                            .user(this)
                            .interestType(interestType)
                            .build())
                    .collect(Collectors.toSet());
        }
    }
}
