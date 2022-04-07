package com.maybank.springboot.library.service;

import java.util.List;

import com.maybank.springboot.library.model.Book;

public interface BookService {
	List<Book> listAllBook();

	List<Book> listAvailableBook();
	
	List<Book> listBookByKeyword(String keyword);

	Book getBookByID(int bookID);

	String updateQuantity(int quantity, int bookID);

	Book saveBook(Book book);
	
	List<Book> findBy(String keyword);
	
	Long jmlBook();
	
	List<Book> lisNotAvailableBook();
	
	String updateQuantityAdmin(int quantity, int bookID);
}
