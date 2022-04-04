package com.maybank.springboot.library.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maybank.springboot.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{
	@Modifying
	@Query(value = "Update book SET quantity = :quantity WHERE book_id = :bookID", nativeQuery = true)
	@Transactional
	void updateQuantity(@Param("quantity") int quantity, @Param("bookID") int bookID);
}
