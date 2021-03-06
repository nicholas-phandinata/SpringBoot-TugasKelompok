package com.maybank.springboot.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maybank.springboot.library.model.Book;
import com.maybank.springboot.library.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	BookRepository bookRepo;


	@Override
	public List<Book> listAllBook() {
		// TODO Auto-generated method stub
		return bookRepo.findAll();
	}

	@Override
	public Book getBookByID(int bookID) {
		// TODO Auto-generated method stub
		return bookRepo.findById(bookID).orElse(null);
	}

	@Override
	public String updateQuantity(int quantity, int bookID) {
		// TODO Auto-generated method stub
		bookRepo.updateQuantity(quantity, bookID);
		return "Update Quantity Successfull";
	}

	@Override
	public Book saveBook(Book book) {
		return bookRepo.save(book);

	}

	@Override
	public List<Book> listAvailableBook() {
		// TODO Auto-generated method stub
		return bookRepo.listAvailableBook();
	}

	@Override
	public List<Book> findBy(String keyword) {
		// TODO Auto-generated method stub
		return bookRepo.findBy(keyword);
	}

	@Override
	public Long jmlBook() {
		// TODO Auto-generated method stub
		return bookRepo.count();
	}

	@Override
	public List<Book> listBookByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return bookRepo.searchBook(keyword);
	}
	
	@Override
	public List<Book> lisNotAvailableBook() {
		// TODO Auto-generated method stub
		return bookRepo.listNotAvailableBook();
	}

	@Override
	public String updateQuantityAdmin(int quantity, int bookID) {
		// TODO Auto-generated method stub
		bookRepo.updateQuantity(quantity, bookID);
		return "Update Quantity Successfull";
	}


}
