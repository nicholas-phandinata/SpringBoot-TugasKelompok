package com.maybank.springboot.library.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maybank.springboot.library.model.Approve;

public interface ApproveRepository extends JpaRepository<Approve, Integer>{
	@Modifying
	@Query(value = "insert into approve "
			+ "(rent_id, account_id, book_id, rent_date, return_date) "
			+ "VALUES (:rentID, :accountID, :bookID, :rentDate, :returnDate)", 
			nativeQuery = true)
	@Transactional
	void addApprove(@Param("rentID") int rentID, 
			@Param("accountID") int accountID, 
			@Param("bookID") int bookID, 
			@Param("rentDate") String rentDate, 
			@Param("returnDate") String returnDate);
	
	@Query("SELECT COUNT(a) FROM Approve a WHERE a.account.account_id = ?1")
    int checkNumberApprove(int accountID);
	
	@Query("SELECT a FROM Approve a WHERE a.account.account_id = ?1 AND a.book.book_id = ?2")
	List<Approve> checkApprove(int accountID, int bookID);

}
