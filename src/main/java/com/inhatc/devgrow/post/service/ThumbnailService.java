package com.inhatc.devgrow.post.service;

import com.inhatc.devgrow.post.dto.PostDto;
import com.inhatc.devgrow.post.dto.ThumbnailDto;
import com.inhatc.devgrow.post.entity.Post;
import com.inhatc.devgrow.post.entity.Thumbnail;
import com.inhatc.devgrow.post.repository.PostRepostiory;
import com.inhatc.devgrow.post.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ThumbnailService {
    private final PostRepostiory postRepository;
    private final ThumbnailRepository thumbnailRepository;
    public ResponseEntity<String> saveThumb(String thumbnailImgLocation, MultipartFile thumbnail, Long postId) throws IOException {
        log.info("PostService saveThumb Start");
        log.info("PostService saveThumb postId : " + postId);
        Post post = postRepository.findById(postId).get();
        log.info("PostService saveThumb postId : " + post);
        try {
            log.info(">>>>>>post thumbnailUpload : " + thumbnail.getOriginalFilename());
            // Generate unique filename
            String fileName = UUID.randomUUID().toString() + "." + thumbnail.getOriginalFilename().split("\\.")[1];
            log.info(">>>>>>post thumbnailUpload : " + fileName);
            // Save image to server filesystem
            File savedFile = new File(thumbnailImgLocation, fileName);

            if (!savedFile.exists()) {
                savedFile.mkdirs(); // Create directories recursively if needed
            }

            thumbnail.transferTo(savedFile);

            // Construct image path
            String imagePath = thumbnailImgLocation + fileName;
            log.info(">>>>>>post thumbnailUpload : " + imagePath);
            // Save image path to database
            // ... (Implement database connection and saving logic)

            Thumbnail thumb = Thumbnail.builder()
                    .post_id(post)
                    .save_folder(thumbnailImgLocation)
                    .save_file(fileName)
                    .original_file(thumbnail.getOriginalFilename())
                    .build();

            thumbnailRepository.save(thumb);

            Thumbnail thumbnailOptional = thumbnailRepository.findByPostId(post).get();
            post.updateThumbnail(thumbnailOptional.getId());
            postRepository.save(post);

            log.info("PostService saveThumb End");

            return ResponseEntity.ok("Image uploaded and path saved: " + imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public void getThumb(List<PostDto> post) {
        List<Post> posts = postRepository.findAll();
        List<ThumbnailDto> thumbnailDtos = new ArrayList<>();
        ThumbnailDto dto;
        for (int i = 0; i < post.size() ; i++) {

            Optional<Thumbnail> thumb = thumbnailRepository.findByPostId(posts.get(i));
            if(thumb.isPresent()){
                dto = ThumbnailDto.builder()
                        .post_id(post.get(i).getId())
                        .save_folder(thumb.get().getSave_folder())
                        .save_file(thumb.get().getSave_file())
                        .original_file(thumb.get().getOriginal_file())
                        .build();
                thumbnailDtos.add(dto);
                post.get(i).setPost_thumbnail(dto);
            }
            log.info("+++++++++++++++++++++++++++++++> thumbnail : " + thumbnailDtos.size());
        }
    }

    public List<ThumbnailDto> getThumbTop3(List<PostDto> post) {
        List<Post> posts = postRepository.findAll();
        List<Post> top3posts = postRepository.findTop3ByOrderByLikeCountDesc();
        List<ThumbnailDto> thumbnailDtos = new ArrayList<>();
        ThumbnailDto dto;
        for (int i = 0; i < post.size() ; i++) {

            Optional<Thumbnail> thumb = thumbnailRepository.findByPostId(top3posts.get(i));
            if(thumb.isPresent()){
                dto = ThumbnailDto.builder()
                        .post_id(post.get(i).getId())
                        .save_folder(thumb.get().getSave_folder())
                        .save_file(thumb.get().getSave_file())
                        .original_file(thumb.get().getOriginal_file())
                        .build();
                thumbnailDtos.add(dto);
                post.get(i).setPost_thumbnail(dto);
            }
            log.info("+++++++++++++++++++++++++++++++> thumbnail : " + thumbnailDtos.size());
        }

        return thumbnailDtos;
    }


}
