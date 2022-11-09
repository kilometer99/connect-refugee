package com.connect.refugee.connectrefugee.controllers;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.connect.refugee.connectrefugee.models.*;
import com.connect.refugee.connectrefugee.repositories.*;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReplyRepository replyRepository;

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id, @RequestBody Post post) {
        Optional<Post> postToUpdate = postRepository.findById(id);
        if (!postToUpdate.isPresent()) {
            return ResponseEntity.ok(List.of(""));
        }
        Post updatedPost = postToUpdate.get();
        updatedPost.setTitle(post.getTitle());
        updatedPost.setDescription(post.getDescription());
        postRepository.save(updatedPost);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<?> deleteCategory(@PathVariable("id") long id) {
        try {
            if (replyRepository.existsByPostId(id)) {
                replyRepository.deleteByPostId(id);
            }
            postRepository.deleteById(id);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable(value = "postId") Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Not found post with id = " + postId);
        }
        Optional<Post> post = postRepository.findById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postId}/all-replies")
    public ResponseEntity<?> getAllReplies(@PathVariable(value = "postId") Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Not found post with id = " + postId);
        }
        List<Reply> replies = replyRepository.findByPostId(postId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping("/{postId}/reply")
    public ResponseEntity<?> addRplyToPost(@PathVariable(value = "postId") Long postId, @RequestBody Reply reply) {
        Reply replyAdded = postRepository.findById(postId).map(post -> {
            reply.setPost(post);
            return replyRepository.save(reply);
        }).orElseThrow(() -> new RuntimeException("No post found with id = " + postId));
        return new ResponseEntity<>(replyAdded, HttpStatus.CREATED);
    }
}