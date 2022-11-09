package com.connect.refugee.connectrefugee.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.connect.refugee.connectrefugee.models.Category;
import com.connect.refugee.connectrefugee.models.Post;
import com.connect.refugee.connectrefugee.repositories.CategoryRepository;
import com.connect.refugee.connectrefugee.repositories.PostRepository;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCurrentCategory(@PathVariable(value = "categoryId") Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewCategory(@RequestBody Category category) {
        Category categoryToAdd = new Category(category.getName(), category.getType());
        Category categoryAdded = categoryRepository.save(categoryToAdd);
        return ResponseEntity.ok(categoryAdded);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id, @RequestBody Category category) {
        Optional<Category> categoryToUpdate = categoryRepository.findById(id);
        if (!categoryToUpdate.isPresent()) {
            return ResponseEntity.ok(List.of(""));
        }
        Category updatedCategory = categoryToUpdate.get();
        updatedCategory.setName(category.getName());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") long id) {
        try {
            categoryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{categoryId}/all-posts")
    public ResponseEntity<?> getAllPosts(@PathVariable(value = "categoryId") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Not found category with id = " + categoryId);
        }
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{categoryId}/add-post")
    public ResponseEntity<?> addNewPost(@PathVariable(value = "categoryId") Long categoryId, @RequestBody Post post) {
        Post postAdded = categoryRepository.findById(categoryId).map(category -> {
            post.setCategory(category);
            return postRepository.save(post);
        }).orElseThrow(() -> new RuntimeException("No category found with id = " + categoryId));
        return new ResponseEntity<>(postAdded, HttpStatus.CREATED);
    }

}
