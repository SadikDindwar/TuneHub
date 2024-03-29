package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Users;

public interface UsersService {
	public String addUser(Users user);

	public boolean emailExists(String email);

	public boolean validateUser(String email, String password);

	public String getRole(String email);

	public Users getUser(String email);

	public void updateUser(Users user);

	public List<Users> fetchAllUsers();

	public Users findByName(String username);

	public void deleteUser(String email);

}