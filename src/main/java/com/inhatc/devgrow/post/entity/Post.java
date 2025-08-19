package com.inhatc.devgrow.post.entity;

import com.inhatc.devgrow.auth.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 만들어줌
//@DynamicUpdate //update 할때 실제 값이 변경됨 컬럼으로만 update 쿼리를 만듬
@Entity // JPA Entity 임을 명시
@Getter // Lombok 어노테이션으로 getter
@Table(name = "post") // 테이블 관련 설정 어노테이션
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id; // 게시물 ID

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member memberId; // 회원ID

	@Column(name = "post_title", nullable = false)
	private String title; // 게시물 제목

	@Column(name = "post_content", nullable = false)
	private String content; // 게시물 내용

	@Column(name = "preview", nullable = false)
	private String preview; // 미리보기용

	@Column(name = "post_tag")
	private String tag; // 게시물 태그

	@Column(name = "post_thumbnail")
	private Long thumbnail; // 대표이미지

	@Column(name = "post_analyzed", columnDefinition = "boolean default false")
	private Boolean analyzed; // 분석여부

	@Column(name = "post_likeCount", columnDefinition = "INT DEFAULT 0")
	private Integer likeCount; // 좋아요 갯수

	@Column(name = "post_visibility")
	private String visibility; // 공개여부

	@Column(name = "insertedDate")
	private String insertedDate; // 생성일

	@Modifying
	public void updateLikeCount(int likecount) {
		this.likeCount = likecount;
	}

	@Modifying
	public void updateThumbnail(Long thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Builder // 생성을 Builder 패턴으로 하기 위해서
	public Post(Member member, String post_title, String post_content, String preview, String post_tag,
			Long post_thumnail, boolean post_analyzed, String post_visibility, String insertedDate, Integer likecount) {
		this.memberId = member;
		this.title = post_title;
		this.content = post_content;
		this.preview = preview;
		this.tag = post_tag;
		this.thumbnail = post_thumnail;
		this.likeCount = likecount;
		this.visibility = post_visibility;
		this.analyzed = post_analyzed;
		this.insertedDate = insertedDate;
	}

}
