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
 
 @Query("SELECT u.firstName FROM User u WHERE u.email= ?1")
 String getfirstName(String email);

 @Query(value = "SELECT COUNT(*) FROM `users_roles`\r\n"
 		+ "	JOIN user ON users_roles.user_id = user.id\r\n"
 		+ "    JOIN role ON users_roles.role_id = role.id\r\n"
 		+ "    WHERE role.name != 'ROLE_ADMIN';", nativeQuery = true)
 Long jmlUser();
}

