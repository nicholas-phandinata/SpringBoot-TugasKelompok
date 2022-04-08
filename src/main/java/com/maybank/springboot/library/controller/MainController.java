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
import com.maybank.springboot.library.model.History;
import com.maybank.springboot.library.service.CategoryService;
import com.maybank.springboot.library.service.HistoryService;

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

	@Autowired
	HistoryService historyService;

	// User


	@RequestMapping("/")
	public String home(Model model, @RequestParam(name="Keyword", defaultValue="") String keyword,
			@Param("filter") String filter) {
		if(!"".equals(keyword)) {
			List<Book> displayBooks = bookService.listBookByKeyword(keyword);
			if(displayBooks.isEmpty()) {
				model.addAttribute("NotFound", "Yes");
			}else {
				model.addAttribute("Books", displayBooks);
			}
		}else {
			List<Category> displayCategory = categoryService.listAllCategory();
			List<Book> displayBooks = bookService.listAvailableBook();
			model.addAttribute("Books", displayBooks);
			model.addAttribute("displayCategory", "Yes");
			model.addAttribute("listCategory", displayCategory);
 			if(filter != null) {
 				List<Book> displayBooks1 = bookService.findBy(filter);
 				model.addAttribute("Books", displayBooks1);
 			}
		}
		return "home";
	}
	
	@RequestMapping("/403")
	public String forbidden() {
		return "403";
	}

	@RequestMapping("rent")
	public String rent(Model model) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);

		List<Rent> displayRent = rentService.listRentByUserID(currentID);
		if(!displayRent.isEmpty()) {
			model.addAttribute("ShowButtons", "yes");
		}
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
		if(displayApprove.isEmpty()) {
			model.addAttribute("Empty", "Yes");
		}
		for (int i = 0; i < displayApprove.size(); i++) {
			  if(displayApprove.get(i).getStatus().contentEquals("approved")) {
				  model.addAttribute("Print", "Yes");
			  }else if(displayApprove.get(i).getStatus().contentEquals("Pending")) {
				  model.addAttribute("Emoji", "Yes");
			  }
			}
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
	
	@RequestMapping("myHistory")
	public String myHistory(Model model, @RequestParam(name="Keyword", defaultValue="") String keyword) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);

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

 		}
 		
	@RequestMapping("/admin")
	public String indexAdmin(Model model) {
		List<Book> displayBooks = bookService.listAllBook();
		Long jmlBooks = bookService.jmlBook();
		Long jmlCat= categoryService.jmlCat();
		Long jmlUser= userService.jmlUser();
		Long jmlHis = historyService.jmlHist();
		List<Approve> jmlWait = approveService.jmlWait();
		List<Rent> top = rentService.listAllRent();

		model.addAttribute("jml_book", jmlBooks);
		model.addAttribute("jml_cat", jmlCat);
		model.addAttribute("jml_user", jmlUser);
		model.addAttribute("category", new Category());
		model.addAttribute("Books", displayBooks);
		model.addAttribute("jml_his", jmlHis);
		model.addAttribute("jmlWait", jmlWait);
		model.addAttribute("top", top);
		
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
		String uploadDir = "/Users/nicholasphandinata/Documents/GitHub/SpringBoot-TugasKelompok/src/main/resources/static/images/"
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
	public String editBook(@ModelAttribute Book book,  @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		
		if(fileName == "") { // jika edit tnapa upload gambar
			Book book2 = bookService.getBookByID(book.getBook_id());
			String fileName1 = book2.getBook_image();
			book.setBook_image(fileName1);
			bookService.saveBook(book);
			
		}else {
			book.setBook_image(fileName);
			Book saveBook1 = bookService.saveBook(book);
			String uploadDir = "/Users/nicholasphandinata/Documents/GitHub/SpringBoot-TugasKelompok/src/main/resources/static/images/"
					+ saveBook1.getBook_id();
			System.out.println("Form Data: " + book);
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
		}		
		
//		bookService.saveBook(book);
		return "redirect:/admin/bookList";
	}

	//Aprroval
	
	@RequestMapping("/admin/approval")
	public String approvalAdmin(Model model) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Long currentID =  userService.getCurrentID(currentUserName);
		System.out.println("id emp "+currentID);
		List<Approve> displayApprove = approveService.listAllApprove();
		
		displayApprove.forEach(data -> {
			if(data.getStatus().equals("approved")) {
				model.addAttribute("return", true);
			}
			if(data.getStatus().equals("Pending")) {
				model.addAttribute("approve", true);
			}
		});

		model.addAttribute("Approves", displayApprove);
		return "/admin/approval";

	}
	
	@RequestMapping("admin/approval/approve-all")
	public String approveAll(@Param("action") String action) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		String currentName = userService.getfirstName(currentUserName);		
		approveService.approveAll(currentName);		
		return "redirect:/admin/approval";
	}
	
	@RequestMapping("admin/approval/return-all")
	public String returnAll(@Param("action") String action) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		String currentName = userService.getfirstName(currentUserName);		
		List<Approve> approves = approveService.listAllApprove();
		approves.forEach(data ->{
			Long ID = data.getUser().getId();
			int bookID = data.getBook().getBook_id();
            int quantity = bookService.getBookByID(bookID).getQuantity() + 1;
			historyService.addHistory("Returned", data.getApprove_id(), 
					data.getRent_id(), ID, 
					data.getBook().getBook_id() , 
					data.getRent_date(), data.getReturn_date(), 
					currentName, quantity);
		});
		return "redirect:/admin/approval";
	}
	
	@RequestMapping("admin/approval/reject-all")
	public String rejectAll(@Param("action") String action) {	
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		String currentName = userService.getfirstName(currentUserName);		
		List<Approve> approves = approveService.listAllApprove();
		approves.forEach(data ->{
			Long ID = data.getUser().getId();
			int bookID = data.getBook().getBook_id();
            int quantity = bookService.getBookByID(bookID).getQuantity() + 1;
			historyService.addHistory("Rejected", data.getApprove_id(), 
					data.getRent_id(), ID, 
					data.getBook().getBook_id() , 
					data.getRent_date(), data.getReturn_date(), 
					currentName, quantity);
		});		
		return "redirect:/admin/approval";
	}
	
	@RequestMapping("/admin/approval/{approveId}")
	public String approval(@PathVariable int approveId) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

		String currentName = userService.getfirstName(currentUserName);
		approveService.updateStatus("approved", currentName, approveId);
		return "redirect:/admin/approval";
	}
	

	@RequestMapping("/admin/history/{approveId}")
	public String history(@PathVariable int approveId, 
			@RequestParam("information") String information) {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		String employee = userService.getfirstName(currentUserName);
		
		Approve approve = approveService.getApproveById(approveId);

		int rentID = approve.getRent_id();
		Long ID = approve.getUser().getId();
		int bookID = approve.getBook().getBook_id();
		String rentDate = approve.getRent_date();
		String returnDate = approve.getReturn_date();
		int quantity = bookService.getBookByID(bookID).getQuantity() + 1;
		
		historyService.addHistory(information, approveId, rentID, ID, bookID, rentDate, returnDate, employee, quantity);
		return "redirect:/admin/approval";
	}
	
	// Report
	@RequestMapping("/admin/report")
	public String report(Model model, @Param(value = "rent_date")String rent_date, 
			@Param("return_date") String return_date) {
//		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
//		Long currentID =  userService.getCurrentID(currentUserName);
////		System.out.println("id emp "+currentID);
		List<History> disHistories = historyService.listAll();
		model.addAttribute("histrory", disHistories);
		if(rent_date != null && return_date != null) {
			List<History> disHistories2 = historyService.searchByDate(rent_date, return_date);
			model.addAttribute("histrory", disHistories2);
		}
		
		return ("/admin/report");

	}
	
	// Report
			@RequestMapping("/admin/stock")
			public String stock(Model model) {
	 			List<Book> displayBooks = bookService.lisNotAvailableBook();
	 			System.out.println("book list" + displayBooks);
	 			model.addAttribute("book", displayBooks);
	 			return "/admin/stock";
			}
			
			// Controller Category Book
			@RequestMapping(value = "/admin/stock", method = RequestMethod.POST)
			public String stockAdmin(@RequestParam("val") int val,@RequestParam("book_id") int book_id, Model model){
				System.out.println("val" +(val));
				System.out.println("bookID"+(book_id));
				bookService.updateQuantityAdmin(val, book_id);
				return "redirect:/admin/stock";

			}
		
	

	// BAGIAN LOGIN
	@GetMapping("/login")
	public String login() {
		return "login";
	}


}
