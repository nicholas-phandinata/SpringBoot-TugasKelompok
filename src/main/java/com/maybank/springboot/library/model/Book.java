package com.maybank.springboot.library.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int book_id;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	private String book_title;
	
	private String book_author;
	
	private String book_publisher;
	
	private int quantity;
	
	private String book_image;

	private String description;
	
}
