package com.maybank.springboot.library.service;

import java.util.List;

import com.maybank.springboot.library.model.Book;

public interface BookService {
	List<Book> listAllBook();

	
	Book listBookByID(int bookID);
	
	String updateQuantity(int quantity, int bookID);
	Book saveBook(Book book);

}
