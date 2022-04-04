package com.maybank.springboot.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.maybank.springboot.library.model.Book;
import com.maybank.springboot.library.model.Rent;
import com.maybank.springboot.library.service.BookService;
import com.maybank.springboot.library.service.RentService;

@Controller
public class MainController {
	@Autowired
	BookService bookService;
	
	@Autowired
	RentService rentService;

	@RequestMapping("/")
	public String home(Model model) {
		List<Book> displayBooks = bookService.listAllBook();
//		System.out.println(displayBooks);
		model.addAttribute("Books", displayBooks);
		return "home";
	}
	
	@RequestMapping("/addBook")
	public String addBook() {
		return "addBook";
	}
	
	@RequestMapping("/rent")
	public String rent(Model model) {
		List<Rent> displayRent = rentService.listAllRent();
//		System.out.println(displayRent);
		model.addAttribute("Rents", displayRent);
		return "rent";
	}
	
	@RequestMapping("addToCart")
	public String addToCart(@RequestParam("bookID") int book_id) {
		Book book = bookService.listBookByID(book_id);
		int quantity = book.getQuantity() - 1;
		int account_id = 1;
		List<Rent> checkRent = rentService.checkRent(account_id, book_id);
		int checkNumberRent = rentService.checkNumberRent(account_id);
		System.out.println("Check Rent: " + checkRent);
		System.out.println("Check Number of Rent: " + checkNumberRent);
		if(checkNumberRent < 3) {
			if(checkRent.isEmpty()) {
				rentService.saveRent(account_id, book_id);
				bookService.updateQuantity(quantity, book_id);
			}else {
				System.out.println("You have already borrowed this book!");
			}
		}else {
			System.out.println("You have already borrowed too much book!");
		}
		return "redirect:/";
	}

}
