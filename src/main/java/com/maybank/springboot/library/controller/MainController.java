package com.maybank.springboot.library.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.maybank.springboot.library.model.Category;
import com.maybank.springboot.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.maybank.springboot.library.model.Approve;
import com.maybank.springboot.library.model.Book;
import com.maybank.springboot.library.model.Rent;
import com.maybank.springboot.library.service.ApproveService;
import com.maybank.springboot.library.service.BookService;

import com.maybank.springboot.library.service.RentService;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {
	@Autowired
	BookService bookService;

	@Autowired
	RentService rentService;

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ApproveService approveService;

	@RequestMapping("/")
	public String home(Model model) {
		List<Book> displayBooks = bookService.listAllBook();
//		System.out.println(displayBooks);
		model.addAttribute("Books", displayBooks);
		return "home";

		
	}
	
	@RequestMapping("rent")
	public String rent(Model model) {
		List<Rent> displayRent = rentService.listAllRent();
//		System.out.println(displayRent);
		model.addAttribute("Rents", displayRent);
		return "rent";
	}
	
	@RequestMapping("addToCart")
	public String addToCart(@RequestParam("bookID") int book_id,
			RedirectAttributes redirAttrs) {
		Book book = bookService.getBookByID(book_id);
		int quantity = book.getQuantity() - 1;
		int account_id = 1;
		
		List<Rent> checkRent = rentService.checkRent(account_id, book_id);
		List<Approve> checkApprove = approveService.checkApprove(account_id, book_id);
		
		int checkNumberRent = rentService.checkNumberRent(account_id);
		int checkNumberApprove = approveService.checkNumberApprove(account_id);
		
		int totalRent = checkNumberRent + checkNumberApprove;
//		System.out.println("Check Rent: " + checkRent);
//		System.out.println("Check Number of Rent: " + totalRent);
		if(totalRent < 3) {
			if(checkRent.isEmpty() & checkApprove.isEmpty()) {
				rentService.saveRent(account_id, book_id);
				bookService.updateQuantity(quantity, book_id);
				redirAttrs.addFlashAttribute("msg_success", "Successfully Added to Cart!");
			}else {
				redirAttrs.addFlashAttribute("msg_danger", "You have already borrowed this book!");
				System.out.println("You have already borrowed this book!");
			}
		}else {
			redirAttrs.addFlashAttribute("msg_danger", "You have already borrowed too much book!");
			System.out.println("You have already borrowed too much book!");
		}
		return "redirect:/";
	}
	
	@RequestMapping("deleteRent/{rentID}")
	public String deleteRent(@PathVariable int rentID) {
		int getBookIDFromRent = rentService.getRentByID(rentID).getBook().getBook_id();
		int BookQuantity = bookService.getBookByID(getBookIDFromRent).getQuantity() + 1;
		rentService.deleteRent(rentID, getBookIDFromRent, BookQuantity);
		return "redirect:../";
	}
	
	
	// Admin
	// Controller admin list book
		@RequestMapping("/admin/bookList")
		public String adminListBook(Model model) {
			List<Book> displayBooks = bookService.listAllBook();
//			System.out.println(displayBooks);
			model.addAttribute("Books", displayBooks);
			return "/admin/book";

		}
		
		// Controller Admin Index
		@RequestMapping("/admin")
		public String indexAdmin(Model model) {
			List<Book> displayBooks = bookService.listAllBook();
//			System.out.println(displayBooks);
			model.addAttribute("category", new Category());
			model.addAttribute("Books", displayBooks);
			return "/admin/index";
		}
		
		// Controller Category Book
		@RequestMapping("/admin/category")
		public String categoryAdmin(Model model) {
			List<Category> dispCategories = categoryService.listAllCategory();
//			System.out.println(displayBooks);
			model.addAttribute("cate", dispCategories);
			model.addAttribute("category", new Category());
			return "/admin/bookCategory";

		}
		

		// Controller Admin ADD BOOK
		@RequestMapping("admin/add-book")
		public String addBook(Model model) {
			List<Category> displayCategory = categoryService.listAllCategory();
			System.out.println("Category" + displayCategory);
			model.addAttribute("listCategory", displayCategory);
			model.addAttribute("book", new Book());
			return "addBook";
		}

		// Controller ADD Method Save Book
		@RequestMapping("/save")
		public String save(@ModelAttribute("book") Book book, @RequestParam("fileImage") MultipartFile multipartFile,
				RedirectAttributes ra) throws IOException {

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			book.setBook_image(fileName);
			Book saveBook1 = bookService.saveBook(book);
			String uploadDir = "C:\\Users\\USER\\Documents\\SpringKelompok\\SpringBoot-TugasKelompok\\src\\main\\resources\\static\\images\\"
					+ saveBook1.getBook_id();

			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				System.out.println(filePath.toFile().getAbsoluteFile());
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new IOException("Could not save uploaded file :" + fileName);
			}

			ra.addFlashAttribute("message", "the brand has been added");

			System.out.println("Form Data: " + bookService);

			return "redirect:/";
		}
		
		@RequestMapping("/admin/save-cat")
		public String save(@ModelAttribute Category category) {
			System.out.println("Form Data: " + category);
			categoryService.saveCat(category);
			return "redirect:/admin/category";
		}

	// BAGIAN LOGIN
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("checkout")
	public String checkout(Model model) {
		List<Approve> displayApprove = approveService.listAllApprove();
		model.addAttribute("Approves", displayApprove);
		return "checkout";
	}
	
	@RequestMapping("addCheckout/{rentID}")
	public String addCheckout(@PathVariable int rentID) {
		int accountID = rentService.getRentByID(rentID).getAccount().getAccount_id();
		int bookID = rentService.getRentByID(rentID).getBook().getBook_id();
		String rentDate = rentService.getRentByID(rentID).getRent_date();
		String returnDate = rentService.getRentByID(rentID).getReturn_date();
		approveService.addApprove(rentID, accountID, bookID, rentDate, returnDate);
		return "redirect:../";
	}

}
