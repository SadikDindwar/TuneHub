package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Song;
import com.example.demo.entities.Users;
import com.example.demo.services.SongService;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {

	@Autowired
	UsersService service;

	@Autowired
	SongService songService;

	@PostMapping("/register")
	public String addUsers(@ModelAttribute Users user, HttpSession session, Model model) {
		if (user.getEmail() == "" || user.getUsername() == "" || user.getPassword() == "") {
			return "registration";
		}
		boolean userStatus = service.emailExists(user.getEmail());
		if (userStatus == false) {
			service.addUser(user);
			System.out.println("User added");
			String role = service.getRole(user.getEmail());
			session.setAttribute("email", user.getEmail());
			if (role.equals("admin")) {
				return "adminHome";
			} else {
				boolean premiumUserStatus = user.isPremium();
				String userName = user.getUsername();
				List<Song> songList = songService.fetchAllSongs();
				model.addAttribute("songs", songList);
				model.addAttribute("isPremium", premiumUserStatus);
				model.addAttribute("userName", userName);
				return "customerHome";
			}

		} else {
			return "registration";
		}
	}

	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session, Model model) {
		try {

			if (service.validateUser(email, password) == true) {
				String role = service.getRole(email);

				session.setAttribute("email", email);

				if (role.equals("admin")) {

					return "adminHome";
				} else {
					Users user = service.getUser(email);

					boolean userStatus = user.isPremium();
					String userName = user.getUsername();
					List<Song> songList = songService.fetchAllSongs();
					model.addAttribute("songs", songList);
					model.addAttribute("isPremium", userStatus);
					model.addAttribute("userName", userName);

					return "customerHome";
				}
			} else {
				return "login";
			}

		} catch (Exception e) {
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {

		// invalidating(deleting) the session when user logout
		session.invalidate();

		return "login";
	}

	///////////////////////////////////////////////////////////////////////

	@GetMapping("/viewUsers")
	public String viewUsers(Model model) {

		List<Users> usersList = service.fetchAllUsers();
		model.addAttribute("usersList", usersList);

		return "displayUsers";

	}

	@PostMapping("/searchUser")
	public String searchUser(@RequestParam("username") String username, Model model) {

		Users user = service.findByName(username);
		model.addAttribute("user", user);
		return "searchUser";

	}
	
    @PostMapping("/dropUser")
    public String dropUser(@RequestParam("email") String email, Model model) {
    	if(email == "") {
    		System.out.println("Field cant be empty.");
    		return "deleteUser";
    	}
    	
    	try {
    	service.deleteUser(email);
    	List<Users> usersList = service.fetchAllUsers();
		model.addAttribute("usersList", usersList);
    	
    	return "displayUsers";
    	}
    	catch(Exception e ) {
    		System.out.println("E-mail doesnot exist.");
    		return "deleteUser";
    	}
	
	
    }
}