package com.maybank.springboot.library.service;

import com.maybank.springboot.library.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listAllCategory();
    List<Category> findBy(String keyword);
    Category saveCat(Category category);
    Category getCategoryById(int editId);
    Long jmlCat();
}
