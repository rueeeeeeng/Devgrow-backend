package com.inhatc.devgrow.post.dto;

import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HeartDto {
    private Long id;
    private Member member;
    private Post post;

    @Builder
    public HeartDto(Member member, Post post){
        this.member = member;
        this.post = post;
    }
}
