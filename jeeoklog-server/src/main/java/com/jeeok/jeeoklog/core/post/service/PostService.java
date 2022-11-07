package com.jeeok.jeeoklog.core.post.service;

import com.jeeok.jeeoklog.common.exception.EntityNotFound;
import com.jeeok.jeeoklog.core.post.domain.Post;
import com.jeeok.jeeoklog.core.post.dto.PostSearchCondition;
import com.jeeok.jeeoklog.core.post.dto.UpdatePostParam;
import com.jeeok.jeeoklog.core.post.repository.PostQueryRepository;
import com.jeeok.jeeoklog.core.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    public static final String POST = "Post";

    private final PostQueryRepository postQueryRepository;
    private final PostRepository postRepository;

    /**
     * 게시물 목록 조회
     */
    public Page<Post> findPosts(PostSearchCondition condition, Pageable pageable) {
        return postQueryRepository.findPosts(condition, pageable);
    }

    /**
     * 게시물 단건 조회
     */
    public Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFound(POST, postId.toString()));
    }

    /**
     * 게시물 저장
     */
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    /**
     * 게시물 수정
     */
    public void updatePost(Long postId, UpdatePostParam param) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFound(POST, postId.toString()));
        findPost.updatePost(param);
    }

    /**
     * 게시물 삭제
     */
    public void deletePost(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFound(POST, postId.toString()));
        postRepository.delete(findPost);
    }
}
