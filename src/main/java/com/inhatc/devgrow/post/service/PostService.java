package com.inhatc.devgrow.post.service;

import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.auth.repository.MemberRepository;
import com.inhatc.devgrow.post.dto.PostDto;
import com.inhatc.devgrow.post.dto.ThumbnailDto;
import com.inhatc.devgrow.post.entity.Post;
import com.inhatc.devgrow.post.repository.PostRepostiory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepostiory postRepository;
    private final MemberRepository memberRepository;
    private final ThumbnailService thumbnailService;

    public Long savePost(PostDto postDto) {
        log.info("PostService savePost Start");
        Member member = memberRepository.findByEmail(postDto.getEmail());
        log.info("PostService Post : " + postDto.getPreview());
        Post post = Post.builder()
                .member(member)
                .post_title(postDto.getPost_title())
                .post_content(postDto.getPost_content())
                .preview((postDto.getPreview()))
                .post_tag(postDto.getPost_tag())
                .post_visibility(postDto.getPost_visibility())
                .likecount(0)
                .post_analyzed((postDto.getPost_analyzed()))
                .insertedDate(postDto.getInsertedDate())
                .build();

        log.info("PostService Post : " + post);

        Post savepost = postRepository.save(post);
        Long postId = savepost.getId();

        log.info("PostService savePost End");
        return postId;
    }

    /**
     * @param category (none : 통합검색, title : 제목, publisher : 작성자, content : 글 내용, tag : 태그)
     * @param content
     * @return
     */
    public List<PostDto> searchPost(String category, String content) {

        List<Post> posts = new ArrayList<>();
        if (Objects.equals(category, "none")) { //통합 검색
            posts = postRepository.searchPostLike(content);
        } else if (Objects.equals(category, "title")) {
            posts = postRepository.findByTitleLike("%" + content + "%");
        } else if (Objects.equals(category, "content")) { //글 내용 검색
            posts = postRepository.findByContentLike("%" + content + "%");
        } else if (Objects.equals(category, "tag")) {
            posts = postRepository.findByTagLike("%" + content + "%");
        } else if (Objects.equals(category, "publisher")) {
            posts = postRepository.searchPostMemberLike("%" + content + "%");
        }

        if (posts.isEmpty()) {
            log.info(">>>>>>post searchPost: 검색 결과가 없습니다.");
            return Collections.emptyList(); // 검색 결과 없으면 빈 리스트 반환
        }

        List<PostDto> searchList = new ArrayList<>();
        PostDto postDto = new PostDto();
        for (int i = 0; i < posts.size(); i++) {
            postDto = PostDto.builder()
                    .id(posts.get(i).getId())
                    .post_title(posts.get(i).getTitle())
                    .post_content(posts.get(i).getContent())
                    .preview(posts.get(i).getPreview())
                    .member(posts.get(i).getMemberId())
                    .date(posts.get(i).getInsertedDate())
                    .build();
            searchList.add(postDto);
        }
        thumbnailService.getThumb(searchList);

        return searchList; // 원본 게시글 리스트 반환 (또는 변환된 DTO 리스트 반환)
    }


    public PostDto postDetail(Long postId) {
        Post post = postRepository.findById(postId).get();
        String tag;
        log.info(String.valueOf("================> tag : " + (Objects.equals(post.getTag(), ""))));
        if(Objects.equals(post.getTag(), "")){
            tag = null;
        }else{
            tag = post.getTag().substring(1, post.getTag().length() - 1);
        }

        PostDto postDto = PostDto.builder()
                .post_title(post.getTitle())
                .post_content(post.getContent())
                .member(post.getMemberId())
                .post_tag(tag)
                .likeCount(post.getLikeCount())
                .date(post.getInsertedDate())
                .build();
        return postDto;
    }

    public List<PostDto> postList(int pageId, int size) {
        Pageable pageable = PageRequest.of(pageId,size);

        Page<Post> posts = postRepository.findByVisibility("public", pageable);
        List<Post> post = posts.getContent();
        List<PostDto> list = new ArrayList<>();
        PostDto postDto;

        ThumbnailDto thumbDto = null;
        for (int i = 0; i < post.size(); i++) {
                    postDto = PostDto.builder()
                    .id(post.get(i).getId())
                    .post_title(post.get(i).getTitle())
                    .post_content(post.get(i).getContent())
                    .preview(post.get(i).getPreview())
                    .date(post.get(i).getInsertedDate())
                    .build();
            list.add(postDto);
        }
//        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(post.get(i).getInsertedDate());

        return list;
    }

    public List<PostDto> getTop3LikedPosts() {
        List<Post> posts = postRepository.findTop3ByOrderByLikeCountDesc();
        List<PostDto> list = new ArrayList<>();
        PostDto postDto;
        log.info("======> : " + posts.getFirst().getThumbnail());
        for (int i = 0; i < posts.size() ; i++) {
            postDto = PostDto.builder()
                    .id(posts.get(i).getId())
                    .member(posts.get(i).getMemberId())
                    .post_title(posts.get(i).getTitle())
                    .post_content(posts.get(i).getContent())
                    .preview(posts.get(i).getPreview())
                    .date(posts.get(i).getInsertedDate())
                    .build();
            list.add(postDto);
        }

        return list;
    }



}
