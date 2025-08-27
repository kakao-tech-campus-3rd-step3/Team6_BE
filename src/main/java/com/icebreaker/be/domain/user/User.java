package com.icebreaker.be.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_mbti", length = 4, nullable = false)
    private MbtiType mbti;

    @Column(name = "user_introduction", length = 500, nullable = false)
    private String introduction;

    @Builder
    public User(String name, String phone, Integer age, MbtiType mbti, String introduction) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.mbti = mbti;
        this.introduction = introduction;
    }
}
