package com.icebreaker.be.infra.persistence.jpa;

import com.icebreaker.be.domain.room.entity.QRoom;
import com.icebreaker.be.domain.room.entity.QRoomParticipant;
import com.icebreaker.be.domain.room.repo.RoomParticipantRepositoryCustom;
import com.icebreaker.be.domain.room.vo.RoomParticipantInterest;
import com.icebreaker.be.domain.user.Interest;
import com.icebreaker.be.domain.user.QUser;
import com.icebreaker.be.domain.user.QUserInterest;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomParticipantRepositoryImpl implements RoomParticipantRepositoryCustom {

    private static final QRoom room = QRoom.room;
    private static final QRoomParticipant roomParticipant = QRoomParticipant.roomParticipant;
    private static final QUser user = QUser.user;
    private static final QUserInterest userInterest = QUserInterest.userInterest;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RoomParticipantInterest> findUserWithInterestsByRoomCode(String roomCode) {
        return fetchUserInterests(roomCode).stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(user.id),
                        this::createInitialSummary,
                        this::mergeInterests))
                .values()
                .stream()
                .toList();
    }

    private List<Tuple> fetchUserInterests(String roomCode) {
        return queryFactory
                .select(user.id, user.introduction, user.mbti, userInterest.interest)
                .from(room)
                .join(room.roomParticipants, roomParticipant)
                .join(roomParticipant.user, user)
                .leftJoin(user.interests, userInterest)
                .where(room.code.eq(roomCode))
                .fetch();
    }

    private RoomParticipantInterest createInitialSummary(Tuple tuple) {
        Interest interest = tuple.get(userInterest.interest);
        Set<Interest> interests = interest != null
                ? Set.of(interest)
                : Set.of();

        return new RoomParticipantInterest(
                tuple.get(user.id),
                new LinkedHashSet<>(interests)
        );
    }

    private RoomParticipantInterest mergeInterests(RoomParticipantInterest existing,
            RoomParticipantInterest newOne) {
        Set<Interest> mergedInterests = new LinkedHashSet<>(existing.interests());
        mergedInterests.addAll(newOne.interests());

        return new RoomParticipantInterest(
                existing.id(),
                mergedInterests
        );
    }
}
