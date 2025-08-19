package com.inhatc.devgrow.post.repository;

import com.inhatc.devgrow.post.dto.PostDto;
import com.inhatc.devgrow.post.entity.Post;
import com.inhatc.devgrow.post.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    Optional<Thumbnail> findByPostId(Post postId);
    Optional<Thumbnail> findByPostId(PostDto postId);
}
