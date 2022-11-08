package com.jeeok.jeeoklog.core.post.controller;

import com.jeeok.jeeoklog.common.json.JsonResult;
import com.jeeok.jeeoklog.core.post.controller.request.SavePostRequest;
import com.jeeok.jeeoklog.core.post.controller.request.UpdatePostRequest;
import com.jeeok.jeeoklog.core.post.controller.response.SavePostResponse;
import com.jeeok.jeeoklog.core.post.controller.response.UpdatePostResponse;
import com.jeeok.jeeoklog.core.post.domain.Post;
import com.jeeok.jeeoklog.core.post.dto.PostDto;
import com.jeeok.jeeoklog.core.post.dto.PostSearchCondition;
import com.jeeok.jeeoklog.core.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    /**
     * 게시물 목록 조회
     */
    @GetMapping
    public JsonResult findPosts(PostSearchCondition condition, Pageable pageable) {
        Page<Post> content = postService.findPosts(condition, pageable);
        List<Object> posts = content.stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
        return JsonResult.OK(posts);
    }

    /**
     * 게시물 단건 조회
     */
    @GetMapping("/{postId}")
    public JsonResult findPost(@PathVariable("postId") Long postId) {
        Post findPost = postService.findPost(postId);
        PostDto post = new PostDto(findPost);
        return JsonResult.OK(post);
    }

    /**
     * 게시물 저장
     */
    @PostMapping
    public JsonResult savePost(@RequestBody @Validated SavePostRequest request) {

        //validate
        request.validate();

        Post savedPost = postService.savePost(request.toPost());

        return JsonResult.OK(new SavePostResponse(savedPost.getId()));
    }

    /**
     * 게시물 수정
     */
    @PutMapping("/{postId}")
    public JsonResult updatePost(@PathVariable("postId") Long postId, @RequestBody UpdatePostRequest request) {

        //validate
        request.validate();

        postService.updatePost(postId, request.toParam());
        Post updatedPost = postService.findPost(postId);

        return JsonResult.OK(new UpdatePostResponse(updatedPost.getId()));
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/{postId}")
    public JsonResult deletePost(@PathVariable("postId") Long postId) {

        postService.deletePost(postId);

        return JsonResult.OK();
    }
}
