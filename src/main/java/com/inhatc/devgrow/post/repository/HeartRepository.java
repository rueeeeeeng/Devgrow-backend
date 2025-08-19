package com.inhatc.devgrow.post.repository;

import com.inhatc.devgrow.post.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByMemberIdAndPostId(Long memberId, Long postId);

    void deleteByMemberIdAndPostId(Long memberId, Long postId);

    int countAllByPostId(Long post_id);

    List<Heart> findByMemberId(Long memberId);
}
