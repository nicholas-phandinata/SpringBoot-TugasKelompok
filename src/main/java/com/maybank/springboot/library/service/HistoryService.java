package com.maybank.springboot.library.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maybank.springboot.library.model.History;

@Service
public interface HistoryService {
	
	String addHistory(String information, int approveID, int rentID, 
			Long ID, int bookID, String rentDate, 
			String returnDate, String employee, int quantity);
	
	List<History> listHistoryByID(Long ID);
}
