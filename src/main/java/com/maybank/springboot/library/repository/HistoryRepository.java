package com.maybank.springboot.library.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maybank.springboot.library.model.History;

public interface HistoryRepository extends JpaRepository<History, Integer>{
	@Modifying
	@Query(value = "insert into history "
			+ "(information, approve_id, rent_id, id, book_id, rent_date, return_date, employee) "
			+ "VALUES (:information, :approveID, :rentID, :ID, :bookID, :rentDate, :returnDate, :employee)", 
			nativeQuery = true)
	@Transactional
	void addHistory(@Param("information") String information,
			@Param("approveID") int approveID,
			@Param("rentID") int rentID, 
			@Param("ID") Long ID, 
			@Param("bookID") int bookID, 
			@Param("rentDate") String rentDate, 
			@Param("returnDate") String returnDate,
			@Param("employee") String employee);
	
	@Query("SELECT h FROM History h WHERE h.user.id = ?1")
	List<History> listHistoryByUserID(Long ID);
	
	@Query("SELECT h FROM History h WHERE h.user.id = ?1 AND CONCAT(h.book.book_title, ' ', h.book.book_author, ' ', h.book.book_publisher) LIKE %?2%")
	List<History> searchHistory(Long ID, String keyword);
	
	@Query(value = "SELECT * FROM `history`"
			+ "	WHERE rent_date > :rent_date AND return_date < :return_date", nativeQuery = true)
	List<History> searchByDate(@Param("rent_date") String rent_date, @Param("return_date") String return_date);
}
