package com.maybank.springboot.library.service;

import com.maybank.springboot.library.model.Category;
import com.maybank.springboot.library.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public List<Category> listAllCategory() {
		return categoryRepository.findAll();
	}

	@Override
	public Category saveCat(Category category) {
		// TODO Auto-generated method stub
		return categoryRepository.save(category);
	}

	@Override
	public Category getCategoryById(int editId) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(editId).orElse(null);

	}
}
