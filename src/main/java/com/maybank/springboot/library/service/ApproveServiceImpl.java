package com.maybank.springboot.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maybank.springboot.library.model.Approve;
import com.maybank.springboot.library.repository.ApproveRepository;
import com.maybank.springboot.library.repository.RentRepository;

@Service
public class ApproveServiceImpl implements ApproveService{
	@Autowired
	ApproveRepository approveRepo;
	
	@Autowired
	RentRepository rentRepo;

	@Override
	public List<Approve> listAllApprove() {
		// TODO Auto-generated method stub
		return approveRepo.findAll();
	}

	@Override
	public String addApprove(int rentID, int accountID, int bookID, 
			String rentDate, String returnDate) {
		// TODO Auto-generated method stub
		approveRepo.addApprove(rentID, accountID, bookID, rentDate, returnDate);
		rentRepo.deleteById(rentID);
		return "New approve added successfully!";
	}

	@Override
	public int checkNumberApprove(int accountID) {
		// TODO Auto-generated method stub
		return approveRepo.checkNumberApprove(accountID);
	}

	@Override
	public List<Approve> checkApprove(int accountID, int bookID) {
		// TODO Auto-generated method stub
		return approveRepo.checkApprove(accountID, bookID);
	}
}
