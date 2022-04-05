package com.maybank.springboot.library.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.maybank.springboot.library.model.Category;
import com.maybank.springboot.library.service.CategoryService;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import com.maybank.springboot.library.model.Book;
import com.maybank.springboot.library.model.Rent;
import com.maybank.springboot.library.service.BookService;

import com.maybank.springboot.library.service.RentService;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class MainController {
	@Autowired
	BookService bookService;

	
	@Autowired
	RentService rentService;

	@Autowired
	CategoryService categoryService;


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

		//test coommit
		//commit 2

	}

	// Controller Admin ADD BOOK
	@RequestMapping("/admin/add-book")
		public String addBook(Model model){
		List<Category> displayCategory = categoryService.listAllCategory();
		System.out.println("Category" + displayCategory);
		model.addAttribute("listCategory", displayCategory);
		model.addAttribute("book", new Book());
		return  "addBook";
	}




	 //Controller ADD Method Save Book
	@RequestMapping("/save")
	public String save(@ModelAttribute("book") Book book,
					   @RequestParam("fileImage")MultipartFile multipartFile,
					   RedirectAttributes ra) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		book.setBook_image(fileName);
		Book saveBook1 =  bookService.saveBook(book);
		String uploadDir = "C:\\Users\\USER\\Documents\\SpringKelompok\\SpringBoot-TugasKelompok\\src\\main\\resources\\static\\images\\" + saveBook1.getBook_id();

		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)){
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			System.out.println(filePath.toFile().getAbsoluteFile());
			Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e){
			throw  new IOException("Could not save uploaded file :" + fileName);
		}

		ra.addFlashAttribute("message", "the brand has been added");

		System.out.println("Form Data: " + bookService);

		return "redirect:/";
	}



}
