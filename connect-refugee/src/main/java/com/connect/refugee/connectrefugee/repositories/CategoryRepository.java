package com.connect.refugee.connectrefugee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.connect.refugee.connectrefugee.models.Category;
public interface CategoryRepository extends JpaRepository<Category, Long> {}
