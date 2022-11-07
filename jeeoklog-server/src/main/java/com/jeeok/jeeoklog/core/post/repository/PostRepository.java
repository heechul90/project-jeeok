package com.jeeok.jeeoklog.core.post.repository;

import com.jeeok.jeeoklog.core.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
