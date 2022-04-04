package com.maybank.springboot.library.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maybank.springboot.library.model.Rent;

public interface RentRepository extends JpaRepository<Rent, Integer> {
	@Modifying
	@Query(value = "insert into rent (account_id,book_id) VALUES (:accountID,:bookID)", nativeQuery = true)
	@Transactional
	void addRent(@Param("accountID") int accountID, @Param("bookID") int bookID);
	
	@Query("SELECT r FROM Rent r WHERE r.account.account_id = ?1 AND r.book.book_id = ?2")
	List<Rent> checkRent(int accountID, int bookID);
	
	@Query("SELECT COUNT(r) FROM Rent r WHERE r.account.account_id = ?1")
    int checkNumberRent(int accountID);
	
}
