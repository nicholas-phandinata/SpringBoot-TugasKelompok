package com.maybank.springboot.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maybank.springboot.library.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
 User findByEmail(String email);
}
