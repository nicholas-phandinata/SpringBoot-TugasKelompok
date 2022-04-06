package com.maybank.springboot.library.service;

import org.springframework.stereotype.Service;

@Service
public interface HistoryService {
	String addHistory(String information, int approveID, int rentID, 
			Long ID, int bookID, String rentDate, 
			String returnDate, String employee, int quantity);

}
