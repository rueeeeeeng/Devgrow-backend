package com.inhatc.devgrow.mypage.service;

import com.inhatc.devgrow.auth.dto.MemberDto;
import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.auth.repository.MemberRepository;
import com.inhatc.devgrow.post.dto.PostDto;
import com.inhatc.devgrow.post.entity.Heart;
import com.inhatc.devgrow.post.entity.Post;
import com.inhatc.devgrow.post.repository.HeartRepository;
import com.inhatc.devgrow.post.repository.PostRepostiory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MyPageService {


    private final PostRepostiory postRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

    public List<PostDto> getModalList(Long memberId) {

        Optional<Member> member = memberRepository.findById(memberId);
        List<PostDto> list = new ArrayList<>();
        if(member.isPresent()){
            List<Post> posts = postRepository.findByMemberId(member.get());
            log.info("========================>mypage : " + posts.size());
            PostDto postDto;
            for (int i = 0; i < posts.size(); i++) {
                postDto = PostDto.builder()
                        .id(posts.get(i).getId())
                        .member(posts.get(i).getMemberId())
                        .post_title(posts.get(i).getTitle())
                        .post_content(posts.get(i).getContent())
                        .preview(posts.get(i).getPreview())
                        .post_visibility(posts.get(i).getVisibility())
                        .date(posts.get(i).getInsertedDate())
                        .build();
                list.add(postDto);
            }
        }else{
            log.info("해당 member가 작성한 post가 존재하지 않습니다.");
            return null;
        }
        log.info("========================>mypage : " + list.size());
        return list;
    }

    public Map<String, Object> getPostList(Long memberId, int page, int pageSize) {
        Optional<Member> member = memberRepository.findById(memberId);

        Pageable pageable = PageRequest.of(page, pageSize);

        log.info("========================>mypage page: " + page);
        List<PostDto> list = new ArrayList<>();
        int size;
        if (member.isPresent()) {
            Page<Post> post = postRepository.findByMemberId(member.get(), pageable);
            List<Post> posts = post.getContent();

            size = (int) Math.ceil((double) postRepository.findByMemberId(member.get()).size() / pageSize);
            log.info("++>" + size);
            PostDto postDto;
            for (int i = 0; i < posts.size(); i++) {
                postDto = PostDto.builder()
                        .id(posts.get(i).getId())
                        .member(posts.get(i).getMemberId())
                        .post_title(posts.get(i).getTitle())
                        .post_content(posts.get(i).getContent())
                        .preview(posts.get(i).getPreview())
                        .post_visibility(posts.get(i).getVisibility())
                        .date(posts.get(i).getInsertedDate())
                        .build();
                list.add(postDto);
            }
        } else {
            log.info("해당 member가 작성한 post가 존재하지 않습니다.");
            return null;
        }
        log.info("========================>mypage : " + list.size());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("totalPages", size);

        return resultMap;
    }


    public Map<String, Object> getFavList(Long memberId, int page, int pageSize) {
        Optional<Member> member = memberRepository.findById(memberId);

        Pageable pageable = PageRequest.of(page, pageSize);

        log.info("========================>mypage page: " + page);
        List<PostDto> list = new ArrayList<>();
        int size;
        if (member.isPresent()) {
            List<Heart> heartsList = heartRepository.findByMemberId(memberId);
            log.info("=========>"+heartsList.getFirst().getPost().getId());


            Post likePost = null;
            List<Post> posts = new ArrayList<>();
            for (int i = 0; i < heartsList.size(); i++) {
                likePost = postRepository.findById(heartsList.get(i).getPost().getId()).get();
                posts.add(likePost);
            }


            size = (int) Math.ceil((double) postRepository.findByMemberId(member.get()).size() / pageSize);
            log.info("++>" + size);
            PostDto postDto;
            for (int i = 0; i < posts.size(); i++) {
                postDto = PostDto.builder()
                        .id(posts.get(i).getId())
                        .member(posts.get(i).getMemberId())
                        .post_title(posts.get(i).getTitle())
                        .post_content(posts.get(i).getContent())
                        .preview(posts.get(i).getPreview())
                        .post_visibility(posts.get(i).getVisibility())
                        .date(posts.get(i).getInsertedDate())
                        .build();
                list.add(postDto);
            }
        } else {
            log.info("해당 member가 작성한 post가 존재하지 않습니다.");
            return null;
        }
        log.info("========================>mypage : " + list.size());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", list);
//        resultMap.put("totalPages", size);

        return resultMap;
    }
    public Member getInfo(String email) {
        Member member = memberRepository.findByEmail(email);
        return member;
    }

    public Member updateProfile(Map<String, Object> param) {
        String nickname = (String) param.get("name");
        String info = (String) param.get("info");
        String email = (String) param.get("email");

        MemberDto dto = new MemberDto();
        if(nickname != null){
            dto.setNickname(nickname);
        }
        if(info != null){
            dto.setInfo(info);
        }


        Member member = memberRepository.findByEmail(email);
        member = member.updateInfo(dto);

        memberRepository.save(member);
        return member;
    }
}
