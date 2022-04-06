package com.maybank.springboot.library.service;

import com.maybank.springboot.library.model.Book;
import com.maybank.springboot.library.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listAllCategory();
    Category saveCat(Category category);
    Category getCategoryById(int editId);
}
