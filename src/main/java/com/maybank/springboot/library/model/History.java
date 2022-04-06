package com.maybank.springboot.library.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "history")
public class History {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int history_id;
	
	private String information;
	
	private String record_time;
	
	private int approve_id;
	
	private int rent_id;
	
	@OneToOne
	@JoinColumn(name = "id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "book_id")
	private Book book;
	
	private String rent_date;
	
	private String return_date;
	
	private String employee;
}
