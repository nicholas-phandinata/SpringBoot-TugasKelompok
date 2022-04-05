package com.maybank.springboot.library.service;

import java.util.List;

import com.maybank.springboot.library.model.Rent;

public interface RentService {
	List<Rent> listAllRent();
	
	String saveRent(int accountID, int bookID);
	
	List<Rent> checkRent(int accountID, int bookID);
	
	int checkNumberRent(int accountID);
	
	Rent getRentByID(int rentID);
	
	String deleteRent(int rentID, int bookID, int quantity);
}
