package com.maybank.springboot.library.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int account_id;
	
	private String first_name;
	
	private String last_name;
	
	private String no_hp;
	
	private String address;
	
	private String email;
	
	private String username;
	
	private String password;
}
