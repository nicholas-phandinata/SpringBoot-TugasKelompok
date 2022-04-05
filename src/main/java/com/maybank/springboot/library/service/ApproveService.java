package com.maybank.springboot.library.service;

import java.util.List;

import com.maybank.springboot.library.model.Approve;

public interface ApproveService {
	List<Approve> listAllApprove();
	
	String addApprove(int rentID, int accountID, int bookID, 
			String rentDate, String returnDate);
	
	int checkNumberApprove(int accountID);
	
	List<Approve> checkApprove(int accountID, int bookID);

}
