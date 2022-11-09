package com.jeeok.jeeoklog;

import com.jeeok.jeeoklog.core.post.repository.PostQueryRepository;
import com.jeeok.jeeoklog.core.post.repository.PostRepository;
import com.jeeok.jeeoklog.core.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class PostTestConfig {

    @PersistenceContext EntityManager em;

    @Autowired PostRepository postRepository;

    @Bean
    public PostService postService() {
        return new PostService(postQueryRepository(), postRepository);
    }

    @Bean
    public PostQueryRepository postQueryRepository() {
        return new PostQueryRepository(em);
    }
}
