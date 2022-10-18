package com.jenga.weather.domain.member.repository;

import com.jenga.weather.domain.member.entity.Member;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Log> {
    Optional<Member> findByUsername(String username);
}
