package com.inhatc.devgrow.mypage.controller;

import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.mypage.service.MyPageService;
import com.inhatc.devgrow.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 마이페이지 분석탭 -> 해당 유저가 작성한 게시물 리스트
     * @param param
     * @return
     */
    @PostMapping( "myPage/selectPost")
    @ResponseBody
    public List<PostDto> selectPost(@RequestBody Map<String, Object> param){
        log.info(">>>>>>post selectPost: " + param);
        Long memberId = Long.parseLong(param.get("memberId").toString());
        List<PostDto> posts =  myPageService.getModalList(memberId);
        log.info("========================>mypage selectPost: " + posts.size());
        return posts;
    }

    @PostMapping( "/myPage/myProfile")
    @ResponseBody
    public Member myProfile(@RequestBody Map<String, Object> param){
        log.info(">>>>>>post myProfile: " + param.get("email"));
        String email = param.get("email").toString();

        Member member = myPageService.getInfo(email);

        return member;
    }

    @PostMapping( "myPage/profileUpdate")
    @ResponseBody
    public Member profileUpdate(@RequestBody Map<String, Object> param){
        log.info(">>>>>>post myProfile: " + param.get("info"));
        log.info(">>>>>>post myProfile: " + param.get("name"));

        Member member = myPageService.updateProfile(param);
        log.info(">>>>>>post myProfile: " + member.getInfo());
        return member;
    }

    @PostMapping( "myPage/myPost")
    public Map<String, Object> getMyPosts(@RequestBody Map<String, Object> param){
        Long memberId = Long.parseLong(param.get("memberId").toString());
        int page = Integer.parseInt(param.get("page").toString())-1;
        log.info("========================>mypage : " + page);
        int pageSize = Integer.parseInt(param.get("pageSize").toString());
        Map<String, Object> myPosts = myPageService.getPostList(memberId, page, pageSize);

        return myPosts;
    }

    @PostMapping( "myPage/myFav")
    public Map<String, Object> getMyFav(@RequestBody Map<String, Object> param){
        log.info("========================>getMyFav : ");
        Long memberId = Long.parseLong(param.get("memberId").toString());
        int page = Integer.parseInt(param.get("page").toString())-1;
        log.info("========================>mypage : " + page);
        int pageSize = Integer.parseInt(param.get("pageSize").toString());
        Map<String, Object> myPosts = myPageService.getFavList(memberId, page, pageSize);

        return myPosts;
    }
}
