package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;

@Controller
public class UsersController {

//	@PostMapping("/register")
//	public String addUsers(@RequestParam("username")String username,
//			@RequestParam("email")String email,
//			@RequestParam("password")String password,
//			@RequestParam("gender")String gender,
//			@RequestParam("role")String role,
//			@RequestParam("address")String address		
//			) {		
//		System.out.println(username+" "+email+" "+password);

	@Autowired
	UsersService service;

	@PostMapping("/register")
	public String addUsers(@ModelAttribute Users user) {
		boolean userStatus = service.emailExists(user.getEmail());
		if (userStatus == false) {
			service.addUser(user);
			System.out.println("User added");

		} else {
			System.out.println("User already exists");
		}

		return "home";
	}

	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email, @RequestParam("password") String password) {
		if (service.validateUser(email, password) == true) {
			String role = service.getRole(email);
			
			if (role.equals("customer") ) {
				return "customerHome";
			} else {
				return "adminHome";
			}
		} else {
			return "login";
		}
	}

}
