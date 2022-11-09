package com.connect.refugee.connectrefugee.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.connect.refugee.connectrefugee.models.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long>  {
    List<Reply> findByPostId(Long postId);
    Boolean existsByPostId(Long postId);
    Long deleteByPostId(Long postId);
    Boolean existsByParentReplyId(Long parentReplyId);
    Long deleteByParentReplyId(Long parentReplyId);
}