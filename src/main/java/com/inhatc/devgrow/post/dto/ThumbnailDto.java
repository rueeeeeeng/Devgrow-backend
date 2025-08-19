package com.inhatc.devgrow.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ThumbnailDto {

    private Long id;        //썸네일 ID

    private Long post_id;        //게시물 ID

    private String save_folder;       //이미지 저장 폴더

    private String original_file;     //원본 이미지 파일명

    private String save_file;     //이미지 파일명



    @Builder //생성을 Builder 패턴으로 하기 위해서
    public ThumbnailDto(Long id, Long post_id, String save_folder, String original_file, String save_file) {
        this.id = id;
        this.post_id = post_id;
        this.save_folder = save_folder;
        this.original_file = original_file;
        this.save_file = save_file;
    }
}
