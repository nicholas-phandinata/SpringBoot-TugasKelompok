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
			+ "(rent_id, id, book_id, rent_date, return_date) "
			+ "VALUES (:rentID, :ID, :bookID, :rentDate, :returnDate)", 
			nativeQuery = true)
	@Transactional
	void addApprove(@Param("rentID") int rentID, 
			@Param("ID") Long ID, 
			@Param("bookID") int bookID, 
			@Param("rentDate") String rentDate, 
			@Param("returnDate") String returnDate);
	
	@Query("SELECT COUNT(a) FROM Approve a WHERE a.user.id = ?1")
    int checkNumberApprove(Long ID);
	
	@Query("SELECT a FROM Approve a WHERE a.user.id = ?1 AND a.book.book_id = ?2")
	List<Approve> checkApprove(Long ID, int bookID);
	
	@Query("SELECT a FROM Approve a WHERE a.user.id = ?1")
	List<Approve> listApproveByUserID(Long ID);
	
	@Modifying
	@Query(value = "Update approve SET status = :status, employee =:employee WHERE  approve_id = :approvedID", nativeQuery = true)
	@Transactional
	void updateStatus(@Param("status") String status, @Param("employee") String firstName ,@Param("approvedID") int approvedID);

}
