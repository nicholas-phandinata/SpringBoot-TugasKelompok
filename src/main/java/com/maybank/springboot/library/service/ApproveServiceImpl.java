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
	public String addApprove(int rentID, Long ID, int bookID, 
			String rentDate, String returnDate) {
		// TODO Auto-generated method stub
		approveRepo.addApprove(rentID, ID, bookID, rentDate, returnDate);
		rentRepo.deleteById(rentID);
		return "New approve added successfully!";
	}

	@Override
	public int checkNumberApprove(Long ID) {
		// TODO Auto-generated method stub
		return approveRepo.checkNumberApprove(ID);
	}

	@Override
	public List<Approve> checkApprove(Long ID, int bookID) {
		// TODO Auto-generated method stub
		return approveRepo.checkApprove(ID, bookID);
	}

	@Override
	public List<Approve> listApproveByID(Long ID) {
		// TODO Auto-generated method stub
		return approveRepo.listApproveByUserID(ID);
	}

	@Override
	public Approve getApproveById(int approveId) {
		// TODO Auto-generated method stub
		return approveRepo.findById(approveId).orElse(null);
	}

	@Override
	public String updateStatus(String status,String firstName, int approvedID) {
		// TODO Auto-generated method stub
		approveRepo.updateStatus(status, firstName, approvedID);
		return "Update Successfull";
	}

}
