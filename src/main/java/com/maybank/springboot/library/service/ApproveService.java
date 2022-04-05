package com.maybank.springboot.library.service;

import java.util.List;

import com.maybank.springboot.library.model.Approve;

public interface ApproveService {
	List<Approve> listAllApprove();
	
	List<Approve> listApproveByID(Long ID);
	
	String addApprove(int rentID, Long ID, int bookID, 
			String rentDate, String returnDate);
	
	int checkNumberApprove(Long ID);
	
	List<Approve> checkApprove(Long ID, int bookID);

}
