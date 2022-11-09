package com.jeeok.jeeoklog.core.post.service;

import com.jeeok.jeeoklog.PostTestConfig;
import com.jeeok.jeeoklog.core.post.domain.Post;
import com.jeeok.jeeoklog.core.post.dto.PostSearchCondition;
import com.jeeok.jeeoklog.core.post.dto.SearchCondition;
import com.jeeok.jeeoklog.core.post.repository.PostQueryRepository;
import com.jeeok.jeeoklog.core.post.repository.PostRepository;
import com.netflix.discovery.converters.Auto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(PostTestConfig.class)
public class PostQueryServiceTest {

    //CREATE_POST
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final int HITS = 0;

    @PersistenceContext EntityManager em;

    @Autowired PostService postService;

    @Autowired PostQueryRepository postQueryRepository;

    private Post getPost(String title, String content) {
        return Post.createPost()
                .title(title)
                .content(content)
                .build();
    }

    @Test
    @DisplayName("게시물 목록 조회")
    void findMembers() {
        //given
        IntStream.range(0, 30).forEach(i -> em.persist(getPost(TITLE + i, CONTENT)));

        PostSearchCondition condition = new PostSearchCondition();
        condition.setSearchCondition(SearchCondition.TITLE);
        condition.setSearchKeyword(TITLE);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<Post> content = postService.findPosts(condition, pageRequest);

        //then
        assertThat(content.getTotalElements()).isEqualTo(30);
        assertThat(content.getContent().size()).isEqualTo(10);
    }
}
