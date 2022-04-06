package com.maybank.springboot.library.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.maybank.springboot.library.model.Category;
import com.maybank.springboot.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.lowagie.text.DocumentException;
import com.maybank.springboot.library.model.Approve;
import com.maybank.springboot.library.model.Book;
import com.maybank.springboot.library.model.Rent;
import com.maybank.springboot.library.model.UserPDFExporter;
import com.maybank.springboot.library.service.ApproveService;
import com.maybank.springboot.library.service.BookService;

import com.maybank.springboot.library.service.RentService;
import com.maybank.springboot.library.service.user.UserService;

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
	
	@Autowired
	UserService userService;



	// User


	@RequestMapping("/")
	public String home(Model model) {
		List<Book> displayBooks = bookService.listAvailableBook();
		
		model.addAttribute("Books", displayBooks);
		return "home";

	}

	@RequestMapping("rent")
	public String rent(Model model) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);

		List<Rent> displayRent = rentService.listRentByUserID(currentID);
//		System.out.println(displayRent);
		model.addAttribute("Rents", displayRent);
		return "rent";
	}

	@RequestMapping("addToCart")
	public String addToCart(@RequestParam("bookID") int book_id,
			RedirectAttributes redirAttrs) {
		Book book = bookService.getBookByID(book_id);
		int quantity = book.getQuantity() - 1;
		
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);
		System.out.println("id "+ currentID);
		System.out.println("userName "+currentUserName);
		List<Rent> checkRent = rentService.checkRent(currentID, book_id);
		List<Approve> checkApprove = approveService.checkApprove(currentID, book_id);
		
		int checkNumberRent = rentService.checkNumberRent(currentID);
		int checkNumberApprove = approveService.checkNumberApprove(currentID);
		
		int totalRent = checkNumberRent + checkNumberApprove;
//		System.out.println("Check Rent: " + checkRent);
//		System.out.println("Check Number of Rent: " + totalRent);
		if(totalRent < 3) {
			if(checkRent.isEmpty() & checkApprove.isEmpty()) {
				rentService.saveRent(currentID, book_id);
				bookService.updateQuantity(quantity, book_id);
				redirAttrs.addFlashAttribute("msg_success", "Successfully Added to Cart!");
			}else {
				redirAttrs.addFlashAttribute("msg_danger", "You have already borrowed this book!");
			}
		}else {
			redirAttrs.addFlashAttribute("msg_danger", "You have already borrowed too much book!");
		}
		return "redirect:/";
	}

	@RequestMapping("deleteRent/{rentID}")
	public String deleteRent(@PathVariable int rentID) {
		int getBookIDFromRent = rentService.getRentByID(rentID).getBook().getBook_id();
		int BookQuantity = bookService.getBookByID(getBookIDFromRent).getQuantity() + 1;
		rentService.deleteRent(rentID, getBookIDFromRent, BookQuantity);
		return "redirect:../rent";
	}
	
	@RequestMapping("checkout")
	public String checkout(Model model) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);
		
		List<Approve> displayApprove = approveService.listApproveByID(currentID);
		model.addAttribute("Approves", displayApprove);
		return "checkout";
	}
	
	@RequestMapping("addCheckout/{rentID}")
	public String addCheckout(@PathVariable int rentID) {
		Long ID = rentService.getRentByID(rentID).getUser().getId();
		int bookID = rentService.getRentByID(rentID).getBook().getBook_id();
		String rentDate = rentService.getRentByID(rentID).getRent_date();
		String returnDate = rentService.getRentByID(rentID).getReturn_date();
		approveService.addApprove(rentID, ID, bookID, rentDate, returnDate);
		return "redirect:../checkout";
	}
	
	@RequestMapping("cancel-all")
	public String cancelAll() {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);
		
		List<Rent> getRent = rentService.listRentByUserID(currentID);
        getRent.forEach((item) -> {
            int rentID = item.getRent_id();
            int bookID = item.getBook().getBook_id();
            int quantity = bookService.getBookByID(bookID).getQuantity() + 1;
            
    		rentService.deleteRent(rentID, bookID, quantity);
        });
		return "redirect:/rent";
	}
	

	@RequestMapping("checkout-all")
	public String checkoutAll() {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);
		
		List<Rent> getRent = rentService.listRentByUserID(currentID);
        getRent.forEach((item) -> {
            int rentID = item.getRent_id();
            int bookID = item.getBook().getBook_id();
            String rentDate = item.getRent_date();
            String returnDate = item.getReturn_date();
            
            approveService.addApprove(rentID, currentID, bookID, rentDate, returnDate);
        });
		return "redirect:/checkout";
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
 			if(keyword != null) {
 				List<Book> displayBooks1 = bookService.findBy(keyword);
 				model.addAttribute("Books", displayBooks1);
 			}
 			
 			return "/admin/book";

 		}
 		
	@RequestMapping("/admin")
	public String indexAdmin(Model model) {
		List<Book> displayBooks = bookService.listAllBook();
		Long jmlBooks = bookService.jmlBook();
		Long jmlCat= categoryService.jmlCat();
		Long jmlUser= userService.jmlUser();
		
		model.addAttribute("jml_book", jmlBooks);
		model.addAttribute("jml_cat", jmlCat);
		model.addAttribute("jml_user", jmlUser);
		model.addAttribute("category", new Category());
		model.addAttribute("Books", displayBooks);
		return "/admin/index";
	}

	// Controller Category Book
	@RequestMapping("/admin/category")
	public String categoryAdmin(Model model, @Param(value = "keyword") String keyword ){
		List<Category> dispCategories = categoryService.listAllCategory();
		model.addAttribute("cate", dispCategories);
		if(keyword !=null) {
			List<Category> dispCategories1 = categoryService.findBy(keyword);
			model.addAttribute("cate", dispCategories1);
		}
		
//			System.out.println(displayBooks);
		
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

		return "redirect:/admin/bookList";
	}

	@RequestMapping("/admin/save-cat")
	public String save(@ModelAttribute Category category) {
		System.out.println("Form Data: " + category);
		categoryService.saveCat(category);
		return "redirect:/admin/category";
	}
	
	@RequestMapping("/admin/edit-cat/{editId}")
	public String showEditCategory(@PathVariable int editId, Model model) {
		Category category =  categoryService.getCategoryById(editId);
		model.addAttribute("category", category);
		System.out.println("data"+ category);
		return "admin/editCategory";
	}
	
	@PostMapping("/admin/edit-cat")
	public String editCategory(@ModelAttribute Category category) {
		System.out.println("Form Data: " + category);
		categoryService.saveCat(category);
		return "redirect:/admin/category";
	}
	
	@RequestMapping("/admin/edit-book/{book_id}")
	public String showEditBook(@PathVariable int book_id, Model model) {
		Book book =  bookService.getBookByID(book_id);
		List<Category> displayCategory = categoryService.listAllCategory();
		model.addAttribute("listCategory", displayCategory);
		model.addAttribute("book", book);
		System.out.println("data"+ book);
		return "admin/editBook";
	}
	
	@PostMapping("/admin/edit-book")
	public String editBook(@ModelAttribute Book book) {
		System.out.println("Form Data: " + book);
		bookService.saveBook(book);
		return "redirect:/admin/bookList";
	}

	//Aprroval
	
	@RequestMapping("/admin/approval")
	public String approvalAdmin(Model model) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);
		System.out.println("id emp "+currentID);
		List<Approve> displayApprove = approveService.listAllApprove();
		model.addAttribute("Approves", displayApprove);
		return "/admin/approval";

	}
	
	@RequestMapping("/admin/approval/{approveId}")
	public String approval(@PathVariable int approveId) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);
		String currentName = userService.getfirstName(currentUserName);
		approveService.updateStatus("approved", currentName, approveId);
		return "redirect:/admin/approval";
	}
	

	// BAGIAN LOGIN
	@GetMapping("/login")
	public String login() {
		return "login";
	}


}
