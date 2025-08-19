package com.inhatc.devgrow.post.dto;

import com.inhatc.devgrow.auth.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private Member member;
    private String email;
    private String post_title;
    private String post_content;
    private String preview;
    private String post_tag;
    private ThumbnailDto post_thumbnail;
    private Boolean post_analyzed;
    private String post_visibility;
    private String insertedDate;
    private boolean like;
    private int likeCount;

    @Builder
    public PostDto(Long id, Member member, String email, String post_title, String post_content,String preview,
                   String post_tag, ThumbnailDto post_thumnail, boolean  post_analyzed,
                   String post_visibility, String date, boolean like, int likeCount){
        this.id = id;
        this.member = member;
        this.email = email;
        this.post_title = post_title;
        this.post_content = post_content;
        this.preview = preview;
        this.post_tag = post_tag;
        this.post_thumbnail = post_thumnail;
        this.post_visibility = post_visibility;
        this.post_analyzed = post_analyzed;
        this.insertedDate = date;
        this.like = like;
        this.likeCount = likeCount;
    }


}
