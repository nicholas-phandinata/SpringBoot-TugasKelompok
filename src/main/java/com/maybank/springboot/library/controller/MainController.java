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
	
	@RequestMapping("addBook")
	public String addBook() {
		return "addBook";
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
		int checkNumberRent = rentService.checkNumberRent(account_id);
		System.out.println("Check Rent: " + checkRent);
		System.out.println("Check Number of Rent: " + checkNumberRent);
		if(checkNumberRent < 3) {
			if(checkRent.isEmpty()) {
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

		//test coommit
		//commit 2

<<<<<<< Updated upstream
	}
=======
		if(!"".equals(keyword)) {
			List<History> displayHistory = historyService.listHistoryByKeyword(currentID, keyword);
			if(displayHistory.isEmpty()) {
				model.addAttribute("NotFound", "Yes");
			}else {
				model.addAttribute("Histories", displayHistory);
			}
		}else {
			List<History> displayHistory = historyService.listHistoryByID(currentID);
			model.addAttribute("Histories", displayHistory);
		}
		return "history";
	}
	
    @GetMapping("export")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=MyCheckout_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);
        
		List<Approve> listApprove = approveService.listApproveByID(currentID);
         
        UserPDFExporter exporter = new UserPDFExporter(listApprove);
        exporter.export(response);
         
    }
	
	
 // -------------------------------Admin------------------------------------
 // Controller admin list book
 		@RequestMapping("/admin/bookList")
 		public String adminListBook(Model model, @Param("keyword") String keyword) {
 			List<Category> displayCategory = categoryService.listAllCategory();
 			List<Book> displayBooks = bookService.listAllBook();
// 			System.out.println("Category" + displayCategory);
 			model.addAttribute("listCategory", displayCategory);
 			model.addAttribute("book", new Book());
 			model.addAttribute("Books", displayBooks);
// 			System.out.println(displayBooks);
 			if(keyword != null) {
 				List<Book> displayBooks1 = bookService.findBy(keyword);
 				model.addAttribute("Books", displayBooks1);
 				System.out.println(displayBooks1);
 			}
 			
 			return "/admin/book";
>>>>>>> Stashed changes

	// Controller Admin ADD BOOK
	@RequestMapping("admin/add-book")
		public String addBook(Model model){
		List<Category> displayCategory = categoryService.listAllCategory();
		System.out.println("Category" + displayCategory);
		model.addAttribute("listCategory", displayCategory);
		model.addAttribute("book", new Book());
		return  "addBook";
	}




	 //Controller ADD Method Save Book
	@RequestMapping("save")
	public String save(@ModelAttribute("book") Book book,
					   @RequestParam("fileImage")MultipartFile multipartFile,
					   RedirectAttributes ra) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		book.setBook_image(fileName);
		Book saveBook1 =  bookService.saveBook(book);
		String uploadDir = "/Users/nicholasphandinata/Documents/GitHub/SpringBoot-TugasKelompok/src/main/resources/static/images/" + saveBook1.getBook_id();

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

	@RequestMapping("deleteRent/{rentID}")
	public String deleteRent(@PathVariable int rentID) {
		int getBookIDFromRent = rentService.getRentByID(rentID).getBook().getBook_id();
		int BookQuantity = bookService.getBookByID(getBookIDFromRent).getQuantity() + 1;
		rentService.deleteRent(rentID, getBookIDFromRent, BookQuantity);
		return "redirect:../";
	}

}
