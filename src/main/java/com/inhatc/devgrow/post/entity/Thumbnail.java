package com.inhatc.devgrow.post.entity;

import com.inhatc.devgrow.post.dto.ThumbnailDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자 만들어줌
//@DynamicUpdate //update 할때 실제 값이 변경됨 컬럼으로만 update 쿼리를 만듬
@Entity //JPA Entity 임을 명시
@Getter //Lombok 어노테이션으로 getter
@Table(name = "thumbnail") //테이블 관련 설정 어노테이션
public class Thumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thumb_id")
    private Long id;        //썸네일 ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post postId;        //게시물 ID

    @Column(name = "save_folder", nullable = false)
    private String save_folder;       //이미지 저장 폴더

    @Column(name = "original_file", nullable = false)
    private String original_file;     //원본 이미지 파일명

    @Column(name = "save_file", nullable = false)
    private String save_file;     //이미지 파일명

    @Builder //생성을 Builder 패턴으로 하기 위해서
    public Thumbnail(Long id, Post post_id, String save_folder, String original_file, String save_file) {
        this.id = id;
        this.postId = post_id;
        this.save_folder = save_folder;
        this.original_file = original_file;
        this.save_file = save_file;
    }

    public ThumbnailDto toThumbnailDto() {
        ThumbnailDto dto = new ThumbnailDto();
        if(dto!= null){
            dto.setId(this.getId());
            dto.setSave_file(this.save_file);
            dto.setOriginal_file(this.original_file);
            dto.setSave_folder(this.save_folder);
            return dto;
        }
        return null;
    }
}
