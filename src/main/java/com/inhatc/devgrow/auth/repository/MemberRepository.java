package com.inhatc.devgrow.auth.repository;

import com.inhatc.devgrow.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndProvider(String email, String provider);

    Member findByEmailAndPassword(String email, String password);
    Member findByEmail(String email);

}