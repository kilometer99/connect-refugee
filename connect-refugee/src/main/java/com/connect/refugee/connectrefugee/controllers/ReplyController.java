package com.connect.refugee.connectrefugee.controllers;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.connect.refugee.connectrefugee.models.*;
import com.connect.refugee.connectrefugee.repositories.*;

@RestController
@RequestMapping("/api/reply")
public class ReplyController {

    @Autowired
    ReplyRepository replyRepository;

    @PostMapping("/{replyId}/reply")
    public ResponseEntity<?> addReplyToRply(@PathVariable(value = "replyId") Long replyId, @RequestBody Reply reply) {
        try {
            Reply replyAdded = replyRepository.findById(replyId).map(replyCheck -> {
                reply.setParentReply(replyCheck);
                reply.setPost(replyCheck.getPost());
                return replyRepository.save(reply);
            }).orElseThrow(() -> new RuntimeException("No reply found with id = " + replyId));
            return new ResponseEntity<>(replyAdded, HttpStatus.CREATED);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReply(@PathVariable("id") long id, @RequestBody Reply reply) {
        Optional<Reply> replyToUpdate = replyRepository.findById(id);
        if (!replyToUpdate.isPresent()) {
            return ResponseEntity.ok("");
        }
        Reply updatedReply = replyToUpdate.get();
        updatedReply.setMessage(reply.getMessage());
        replyRepository.save(updatedReply);
        return ResponseEntity.ok(updatedReply);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<?> deleteReply(@PathVariable("id") long id) {
        try {
            if (replyRepository.existsByParentReplyId(id)) {
                replyRepository.deleteByParentReplyId(id);
            }
            replyRepository.deleteById(id);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}