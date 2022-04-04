package com.maybank.springboot.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maybank.springboot.library.model.Rent;
import com.maybank.springboot.library.repository.RentRepository;

@Service
public class RentServiceImpl implements RentService{
	
	@Autowired
	RentRepository rentRepo;

	@Override
	public List<Rent> listAllRent() {
		// TODO Auto-generated method stub
		return rentRepo.findAll();
	}

	@Override
	public String saveRent(int accountID, int bookID) {
		// TODO Auto-generated method stub
		rentRepo.addRent(accountID, bookID);
		return "New rent added successfully!";
	}

	@Override
	public List<Rent> checkRent(int accountID, int bookID) {
		// TODO Auto-generated method stub
		return rentRepo.checkRent(accountID, bookID);
	}

	@Override
	public int checkNumberRent(int accountID) {
		// TODO Auto-generated method stub
		return rentRepo.checkNumberRent(accountID);
	}

}
