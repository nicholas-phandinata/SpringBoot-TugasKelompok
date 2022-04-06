package com.maybank.springboot.library.service;

import java.util.List;

import com.maybank.springboot.library.model.Book;
import com.maybank.springboot.library.model.Category;

public interface BookService {
	List<Book> listAllBook();

	List<Book> listAvailableBook();

	Book getBookByID(int bookID);

	String updateQuantity(int quantity, int bookID);

	Book saveBook(Book book);

}
