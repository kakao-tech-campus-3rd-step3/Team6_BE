package com.icebreaker.be.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findAllByNameIn(List<String> names);
}
