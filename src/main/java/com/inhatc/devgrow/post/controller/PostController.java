package com.inhatc.devgrow.post.controller;

import com.inhatc.devgrow.post.dto.PostDto;
import com.inhatc.devgrow.post.entity.Heart;
import com.inhatc.devgrow.post.service.HeartService;
import com.inhatc.devgrow.post.service.PostService;
import com.inhatc.devgrow.post.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class PostController {

    @Value("${ThumbnailImgLocation}")
    private String ThumbnailImgLocation;

    private final PostService postService;

    private final HeartService heartService;

    private final ThumbnailService thumbnailService;

    @PostMapping( "post/postEditor")
    @ResponseBody
    public Long editPost(@RequestBody Map<String, Object> param){
        Map<String, Object> resultMap = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PostDto postDto = PostDto.builder()
                .email(param.get("email").toString())
                .post_title(param.get("title").toString())
                .post_content(param.get("content").toString())
                .preview(param.get("previewContent").toString())
                .post_tag(param.get("tag").toString())
                .post_visibility(param.get("visibility").toString())
                .date(LocalDateTime.now().format(formatter))
                .build();

        return postService.savePost(postDto);
    }

    @PostMapping(path ="post/thumbnailUpload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public  ResponseEntity<String> thumbnailUpload(@RequestPart("Thumbnail") MultipartFile thumbnail, @RequestParam("postId") Long postId) throws IOException {
        String oriImgName = thumbnail.getOriginalFilename();        //원본 이미지 이름
        ResponseEntity<String> result = thumbnailService.saveThumb(ThumbnailImgLocation, thumbnail, postId);
        return result;
    }

    @PostMapping("/postList")
    public  Map<String, Object> postList(@RequestBody Map<String, Object> param){
        int pageId  = (int) param.get("pageId");
        List<PostDto> post = postService.postList(pageId,5);
        List<PostDto> top3post = postService.getTop3LikedPosts();

        thumbnailService.getThumb(post);
        thumbnailService.getThumb(top3post);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("post", post);
        resultMap.put("top3post", top3post);
        return resultMap;
    }




    @PostMapping("/postDetail")
    @ResponseBody
    public PostDto postDetail(@RequestBody Map<String, Object> param){
        //post 상세페이지 데이터
        Long postId = Long.parseLong(param.get("postId").toString());
        Long memberId = Long.parseLong(param.get("memberId").toString());
        PostDto postDto = postService.postDetail(postId);
        log.info(">>>>>>post detail likecount: " + postDto.getLikeCount());
        //현재 접속 유저의 해당 게시물 좋아요 여부
        Heart heart = heartService.getLike(postId, memberId);

        if(heart != null){
            postDto.setLike(true);
        }
        log.info(">>>>>>post detail: " + postDto.getMember());
        log.info(">>>>>>post detail likecount: " + postDto.getLikeCount());
        return postDto;
    }

    @PostMapping("post/postSearch")
    @ResponseBody
    public List<PostDto> postSearch(@RequestBody Map<String, Object> param){
        List<PostDto> posts = postService.searchPost(param.get("category").toString(), param.get("searchPost").toString());

        return posts;
    }

    @PostMapping("post/postLike")
    @ResponseBody
    public List<PostDto> postLike(@RequestBody Map<String, Object> param){
        log.info(">>>>>>postLike: " + param.get("isLiked").toString());
        boolean isLiked = (boolean) param.get("isLiked");
        if(!isLiked){
            log.info("저장 isLiked : " + isLiked);
            heartService.saveLike(param);
        }else{
            log.info("삭제 isLiked :  " + isLiked);
            heartService.deleteLike(param);
        }
        return null;
    }
}
