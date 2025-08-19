package com.inhatc.devgrow.post.service;

import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.auth.repository.MemberRepository;
import com.inhatc.devgrow.post.entity.Heart;
import com.inhatc.devgrow.post.entity.Post;
import com.inhatc.devgrow.post.repository.HeartRepository;
import com.inhatc.devgrow.post.repository.PostRepostiory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HeartService {

    private final PostRepostiory postRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

    /**
     * 좋아요 저장하는 기능
     *
     * @param param
     */
    public void saveLike(Map<String, Object> param) {

        Long memberId = Long.parseLong(param.get("memberId").toString());
        Long postId = Long.parseLong(param.get("postId").toString());
        Optional<Member> member = memberRepository.findById(memberId);
        Post post = postRepository.findById(postId).get();
        
        //좋아요 저장
        Heart heart = Heart.builder()
                .member(member.get())
                .post(post)
                .build();
        heartRepository.save(heart);

        //post 테이블에 해당 postId 게시물 좋아요수 저장
        int likecount = heartRepository.countAllByPostId(postId);
        log.info(">>>>>>postLike: likecount  " + likecount);
        post.updateLikeCount(likecount);
        log.info(">>>>>>postLike: post  " + post.getLikeCount());
        postRepository.save(post);


        log.info(">>>>>>postLike: post  " + post.getLikeCount());
        log.info(">>>>>>postLike: " + param.get("memberId"));
        log.info(">>>>>>postLike: " + member.get().getId());
        log.info(">>>>>>postLike: " + param);


    }

    public Heart getLike(Long postId, Long memberId) {
        Heart heart = heartRepository.findByMemberIdAndPostId(memberId, postId);
        return heart;
    }

    public void deleteLike(Map<String, Object> param) {
        Long memberId = Long.parseLong(param.get("memberId").toString());
        Long postId = Long.parseLong(param.get("postId").toString());
        //좋아요 취소
        heartRepository.deleteByMemberIdAndPostId(memberId, postId);

        Post post = postRepository.findById(postId).get();
        //좋아요 취소 후 post 테이블 좋아요 갯수 갱신
        int likeCount = heartRepository.countAllByPostId(postId);
        post.updateLikeCount(likeCount);
        log.info(">>>>>>postLike: post  " + post.getLikeCount());
        postRepository.save(post);
    }
}
