package com.connect.refugee.connectrefugee.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.connect.refugee.connectrefugee.models.Post;

public interface PostRepository extends JpaRepository<Post, Long>  {
    List<Post> findByCategoryId(Long postId);
    
}