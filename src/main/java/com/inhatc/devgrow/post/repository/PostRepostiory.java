package com.inhatc.devgrow.post.repository;

import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepostiory extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p FROM Post p JOIN p.memberId m " +
            "WHERE p.title LIKE %:content% OR p.content LIKE %:content% " +
            "OR p.tag LIKE %:content% OR m.nickname LIKE %:content%")
    List<Post> searchPostLike(@Param("content") String content);        //통합검색

    List<Post> findByContentLike(String content);                       //글 내용 검색

    List<Post> findByTitleLike(String content);                         //제목 검색

    List<Post> findByTagLike(String content);                           //태그 검색

    @Query("SELECT DISTINCT p FROM Post p JOIN p.memberId m " +
            "WHERE m.nickname LIKE %:content%")
    List<Post> searchPostMemberLike(@Param("content") String content);  //작성자 검색

    List<Post> findTop3ByOrderByLikeCountDesc();                   //좋아요 상위 3 포스트

    List<Post> findByMemberId(Member memberId);                     //디폴트 포스트리스트

    Page<Post> findByMemberId(Member memberId, Pageable pageable);      //페이징 처리 포스트리스트

    Page<Post> findByVisibility(String visibility, Pageable pageable);
}
