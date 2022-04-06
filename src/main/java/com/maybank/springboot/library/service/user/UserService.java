package com.maybank.springboot.library.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.maybank.springboot.library.dto.UserRegistrationDto;
import com.maybank.springboot.library.model.User;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
 	
	Long getCurrentID(String email);
}