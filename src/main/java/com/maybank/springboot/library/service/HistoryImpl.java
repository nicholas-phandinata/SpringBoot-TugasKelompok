package com.maybank.springboot.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maybank.springboot.library.model.Approve;
import com.maybank.springboot.library.model.History;
import com.maybank.springboot.library.repository.ApproveRepository;
import com.maybank.springboot.library.repository.BookRepository;
import com.maybank.springboot.library.repository.HistoryRepository;

@Service
public class HistoryImpl implements HistoryService{
	
	@Autowired
	HistoryRepository historyRepo;
	
	@Autowired
	ApproveRepository approveRepo;
	
	@Autowired
	BookRepository bookRepo;
	
	@Override
	public String addHistory(String information, int approveID, int rentID, Long ID, int bookID, String rentDate,
			String returnDate, String employee, int quantity) {
		// TODO Auto-generated method stub
		historyRepo.addHistory(information, approveID, rentID, ID, bookID, rentDate, returnDate, employee);
		approveRepo.deleteById(approveID);
		bookRepo.updateQuantity(quantity, bookID);
		return "History successfully added!";
	}

	@Override
	public List<History> listHistoryByID(Long ID) {
		// TODO Auto-generated method stub
		return historyRepo.listHistoryByUserID(ID);
	}

	@Override
	public List<History> listHistoryByKeyword(Long ID, String keyword) {
		// TODO Auto-generated method stub
		return historyRepo.searchHistory(ID, keyword);
	}

	@Override
	public List<History> listAll() {
		// TODO Auto-generated method stub
		return historyRepo.findAll();
	}

	@Override
	public List<History> searchByDate(String rent_date, String return_date) {
		
		return historyRepo.searchByDate(rent_date, return_date);
	}
		
}
