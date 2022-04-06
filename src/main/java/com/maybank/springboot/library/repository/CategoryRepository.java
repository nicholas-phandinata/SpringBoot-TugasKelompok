package com.maybank.springboot.library.repository;

import com.maybank.springboot.library.model.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query(value = "SELECT * FROM category c WHERE CONCAT(c.category_name) LIKE %?1%", nativeQuery = true)
	public List<Category> search(String keyword);
	
	@Query(value = "SELECT COUNT(*) FROM `category`", nativeQuery = true)
	public Long jmlCat();
}