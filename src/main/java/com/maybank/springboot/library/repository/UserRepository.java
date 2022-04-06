package com.maybank.springboot.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maybank.springboot.library.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

 User findByEmail(String email);
 
 @Query("SELECT u.id FROM User u WHERE u.email = ?1")
 Long getCurrentID(String email);

}
