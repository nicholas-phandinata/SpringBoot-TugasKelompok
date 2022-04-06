package com.maybank.springboot.library.service;

import java.util.List;

import com.maybank.springboot.library.model.Rent;

public interface RentService {
	List<Rent> listAllRent();
	
	String saveRent(Long ID, int bookID);
	
	List<Rent> checkRent(Long ID, int bookID);
	
	List<Rent> listRentByUserID(Long ID);
	
	int checkNumberRent(Long ID);
	
	Rent getRentByID(int RentID);
	
	String deleteRent(int RentID, int bookID, int quantity);
}
