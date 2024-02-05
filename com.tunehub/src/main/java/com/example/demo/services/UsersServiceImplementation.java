package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Users;
import com.example.demo.repositories.UsersRepository;

@Service
public class UsersServiceImplementation implements UsersService {
	@Autowired
	UsersRepository repo;

	@Override
	public String addUser(Users user) {
		repo.save(user);
		return "User Added Successfully";
	}

	@Override
	public boolean emailExists(String email) {
		if (repo.findByEmail(email) == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean validateUser(String email, String password) {
		Users user = repo.findByEmail(email);
		String db_pass = user.getPassword();
		if (password.equals(db_pass)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getRole(String email) {
		Users user = repo.findByEmail(email);

		return user.getRole();
	}

	@Override
	public Users getUser(String email) {
		return repo.findByEmail(email);
	}

	@Override
	public void updateUser(Users user) {
		repo.save(user);

	}

	@Override
	public List<Users> fetchAllUsers() {

		return repo.findAll();
	}

	@Override
	public Users findByName(String username) {
		return repo.findByUsername(username);
	}

	@Override
	public void deleteUser(String email) {
		Users user = repo.findByEmail(email);
		if(user.getRole().equals("customer")) {
			System.out.println(user.getUsername() + " deleted.");
			repo.delete(user);
		}
		else {
			System.out.println("Admins cannot be deleted...");
		}

	}

}