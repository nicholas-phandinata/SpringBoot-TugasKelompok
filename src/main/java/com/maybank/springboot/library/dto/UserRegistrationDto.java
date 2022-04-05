package com.maybank.springboot.library.dto;

import javax.management.loading.PrivateClassLoader;

import lombok.Data;
@Data
public class UserRegistrationDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

    public UserRegistrationDto() {

    }

	public UserRegistrationDto(String firstName, String lastName, String email, String password, String role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
	}   

}
