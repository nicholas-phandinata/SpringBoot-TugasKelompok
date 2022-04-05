package com.maybank.springboot.library.service.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maybank.springboot.library.model.Role;
import com.maybank.springboot.library.repository.RoleRepository;

@Service
@Transactional
public class RoleService {
	@Autowired
	private RoleRepository repository;
	public List<Role> listAll(){
		System.out.println("hasil: " + repository.findAll());
		return repository.findAll();
	}
}
