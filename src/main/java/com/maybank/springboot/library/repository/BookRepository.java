package com.maybank.springboot.library.repository;

import java.util.List;

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
	
	@Query("SELECT b FROM Book b WHERE b.quantity > 0")
	List<Book> listAvailableBook();
	
	@Query(value = "SELECT * FROM `book` WHERE category_id = ?1", nativeQuery = true)
	List<Book> findBy(String keyword);
	
	@Query("SELECT b FROM Book b WHERE CONCAT(b.book_title, ' ', b.book_author, ' ', b.book_publisher) LIKE %?1%")
	public List<Book> searchBook(String keyword);
	
	@Query("SELECT b FROM Book b WHERE b.quantity < 1")
	List<Book> listNotAvailableBook();
	
	@Query(value = "Update book SET quantity = :quantity WHERE book_id = :bookID", nativeQuery = true)
	void updateQuantityAdmin(@Param("quantity") int quantity, @Param("bookID") int bookID);
	
}
