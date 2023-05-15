package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.repository.RicettaRepository;

public class HomeController {
	@Autowired 
	private RicettaRepository ricettaRepository;
	
	@GetMapping(value = "/index") 
	public String index () {
		return "index.html";
	}
}
