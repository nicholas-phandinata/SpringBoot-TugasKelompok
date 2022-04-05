package com.maybank.springboot.library.repository;

import com.maybank.springboot.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}