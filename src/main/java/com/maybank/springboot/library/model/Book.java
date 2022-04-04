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
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int book_id;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	private String book_title;
	
	private String book_author;
	
	private String book_publisher;
	
	private int quantity;
	
}
