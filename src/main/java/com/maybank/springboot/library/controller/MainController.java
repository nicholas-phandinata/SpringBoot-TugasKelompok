package com.maybank.springboot.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maybank.springboot.library.model.Book;
import com.maybank.springboot.library.service.BookService;

@Controller
public class MainController {
	@Autowired
	BookService bookService;
	
	@RequestMapping("/")
	public String home(Model model) {
		List<Book> displayBooks = bookService.listAllBook();
		System.out.println(displayBooks);
		model.addAttribute("Books", displayBooks);
		return "home";
	}
}
