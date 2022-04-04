package com.maybank.springboot.library.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "rent")
public class Rent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rent_id;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
	
	private String rent_date;
	
	private String return_date;
	
	private int fines;
}
