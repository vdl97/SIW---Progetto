package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.repository.RicettaRepository;

@Controller
public class RicettaController {
	@Autowired 
	private RicettaRepository ricettaRepository;
	
	@GetMapping(value = "/index") 
	public String index () {
		return "index.html";
	}

}
