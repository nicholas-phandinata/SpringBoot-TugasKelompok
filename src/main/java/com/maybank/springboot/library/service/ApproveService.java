package com.maybank.springboot.library.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maybank.springboot.library.model.Approve;

@Service
public interface ApproveService {
	List<Approve> listAllApprove();
	
	List<Approve> listApproveByID(Long ID);
	
	String addApprove(int rentID, Long ID, int bookID, 
			String rentDate, String returnDate);
	
	int checkNumberApprove(Long ID);
	
	List<Approve> checkApprove(Long ID, int bookID);
	
	Approve getApproveById(int approveId);
	
	String updateStatus(String status, String firstName, int approvedID);
	
	String approveAll(String emp);
	
	List<Approve> jmlWait();
}
