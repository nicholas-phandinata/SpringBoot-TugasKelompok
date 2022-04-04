package com.maybank.springboot.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maybank.springboot.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

}
