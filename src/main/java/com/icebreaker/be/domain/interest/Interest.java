package com.icebreaker.be.domain.interest;

import com.icebreaker.be.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_interest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_interest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_type", length = 255, nullable = false)
    private InterestType interestType;

    @Builder
    public Interest(User user, InterestType interestType) {
        this.user = user;
        this.interestType = interestType;
    }
}
