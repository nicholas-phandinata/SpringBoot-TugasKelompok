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
	@Query(value = "insert into rent (id,book_id) VALUES (:ID,:bookID)", nativeQuery = true)
	@Transactional
	void addRent(@Param("ID") Long ID, @Param("bookID") int bookID);
	
	@Query("SELECT r FROM Rent r WHERE r.user.id = ?1 AND r.book.book_id = ?2")
	List<Rent> checkRent(Long ID, int bookID);
	
	@Query("SELECT COUNT(r) FROM Rent r WHERE r.user.id = ?1")
    int checkNumberRent(Long ID);
	
	@Query("SELECT r FROM Rent r WHERE r.user.id = ?1")
	List<Rent> listRentByUserID(Long ID);
	
}
