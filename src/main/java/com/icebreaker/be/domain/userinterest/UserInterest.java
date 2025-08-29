package com.icebreaker.be.domain.userinterest;

import com.icebreaker.be.domain.interest.Interest;
import com.icebreaker.be.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_interest_rel",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_interest_unique",
                        columnNames = {"user_id", "interest_id"}
                )
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id", nullable = false)
    private Interest interest;

    @Builder
    public UserInterest(User user, Interest interest) {
        this.user = user;
        this.interest = interest;
    }
}
