package com.jeeok.jeeoklog.core.post.service;

import com.jeeok.jeeoklog.common.exception.EntityNotFound;
import com.jeeok.jeeoklog.core.post.domain.Post;
import com.jeeok.jeeoklog.core.post.dto.PostSearchCondition;
import com.jeeok.jeeoklog.core.post.dto.SearchCondition;
import com.jeeok.jeeoklog.core.post.dto.UpdatePostParam;
import com.jeeok.jeeoklog.core.post.repository.PostQueryRepository;
import com.jeeok.jeeoklog.core.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    //CREATE_POST
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final int HITS = 0;

    //UPDATE_POST
    public static final String UPDATE_TITLE = "update_title";
    public static final String UPDATE_CONTENT = "update_content";

    //ERROR_MESSAGE
    public static final String POST = "Post";
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    @InjectMocks PostService postService;

    @Mock PostQueryRepository postQueryRepository;

    @Mock PostRepository postRepository;


    private Post getPost(String title, String content) {
        return Post.createPost()
                .title(title)
                .content(content)
                .build();
    }

    @Test
    @DisplayName("게시물 목록  검색")
    void findPosts() {
        //given
        List<Post> posts = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> posts.add(getPost(TITLE + i, CONTENT)));
        given(postQueryRepository.findPosts(any(PostSearchCondition.class), any(Pageable.class))).willReturn(new PageImpl<>(posts));


        PostSearchCondition condition = new PostSearchCondition();
        condition.setSearchCondition(SearchCondition.TITLE);
        condition.setSearchKeyword(TITLE);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<Post> content = postQueryRepository.findPosts(condition, pageRequest);

        //then
        assertThat(content.getTotalElements()).isEqualTo(5);
        assertThat(content.getContent().size()).isEqualTo(5);
        assertThat(content.getContent()).extracting("title").contains(TITLE + "0", TITLE + "4");

        //verify
        verify(postQueryRepository, times(1)).findPosts(any(PostSearchCondition.class), any(Pageable.class));
    }

    @Test
    @DisplayName("게시물 단건 검색")
    void findPost() {
        //given
        Post post = getPost(TITLE, CONTENT);
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(post));

        //when
        Post findPost = postService.findPost(0L);

        //then
        assertThat(findPost.getTitle()).isEqualTo(TITLE);
        assertThat(findPost.getContent()).isEqualTo(CONTENT);
        assertThat(findPost.getHits()).isEqualTo(HITS + 1);

        //verify
        verify(postRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("게시물 저장")
    void savePost() {
        //given
        Post post = getPost(TITLE, CONTENT);
        given(postRepository.save(any(Post.class))).willReturn(post);

        //when
        Post savedPost = postService.savePost(post);

        //then
        assertThat(savedPost.getTitle()).isEqualTo(TITLE);
        assertThat(savedPost.getContent()).isEqualTo(CONTENT);
        assertThat(savedPost.getHits()).isEqualTo(HITS);

        //verify
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 수정")
    void updatePost() {
        //given
        Post post = getPost(TITLE, CONTENT);
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(post));

        UpdatePostParam param = UpdatePostParam.builder()
                .title(UPDATE_TITLE)
                .content(UPDATE_CONTENT)
                .build();

        //when
        postService.updatePost(0L, param);

        //then
        assertThat(post.getTitle()).isEqualTo(UPDATE_TITLE);
        assertThat(post.getContent()).isEqualTo(UPDATE_CONTENT);
        assertThat(post.getHits()).isEqualTo(HITS);

        //verify
        verify(postRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePost() {
        //given
        Post post = getPost(TITLE, CONTENT);
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(post));

        //when
        postService.deletePost(0L);

        //then

        //verify
        verify(postRepository, times(1)).findById(any(Long.class));
        verify(postRepository, times(1)).delete(any(Post.class));
    }

    @Test
    @DisplayName("findById_예외")
   void findById_entityNotException() {
        //given
        given(postRepository.findById(any(Long.class))).willThrow(new EntityNotFound(POST, NOT_FOUND_ID.toString()));

        //expected
        assertThatThrownBy(() -> postService.findPost(NOT_FOUND_ID))
                .isInstanceOf(EntityNotFound.class)
                .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + POST)
                .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);

        assertThatThrownBy(() -> postService.updatePost(NOT_FOUND_ID, any(UpdatePostParam.class)))
                .isInstanceOf(EntityNotFound.class)
                .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + POST)
                .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);

        assertThatThrownBy(() -> postService.deletePost(NOT_FOUND_ID))
                .isInstanceOf(EntityNotFound.class)
                .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + POST)
                .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);

        //verify
        verify(postRepository, times(3)).findById(any(Long.class));
    }
}