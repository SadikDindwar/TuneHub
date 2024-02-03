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
import com.example.demo.services.SongService;

@Controller
public class SongController {

	@Autowired
	SongService service;

	@PostMapping("/addSong")
	public String addSong(@ModelAttribute Song song) {
		if (song.getName() == "" || song.getLink() == "") {
			System.out.println("Fields cant be empty.");
			return "newSong";
		}
		boolean songStatus = service.nameExists(song.getName());
		if (songStatus == false) {
			service.addSong(song);
		} else {
			System.out.println("Song name already exists.");
			return "newSong";
		}
		return "adminHome";
	}

	@GetMapping("/viewSongs")
	public String viewSongs(Model model) {
		List<Song> songsList = service.fetchAllSongs();
		model.addAttribute("songs", songsList);

		return "displaySongs";
	}

	@GetMapping("/playSongs")
	public String playSongs(Model model) {
		boolean premiumUser = true;
		if (premiumUser) {
			List<Song> songsList = service.fetchAllSongs();
			model.addAttribute("songs", songsList);

			return "displaySongs";

		} else {
			return "makePayment";
		}

	}

	@PostMapping("/searchSong")
	public String searchSong(@RequestParam("name") String name, Model model) {
		Song song = service.findByName(name);
		// System.out.println(song);
		model.addAttribute("song", song);
		return "searchSong";
	}

}