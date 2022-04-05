package com.maybank.springboot.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maybank.springboot.library.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{
	
}
