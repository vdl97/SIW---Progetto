package it.uniroma3.siw.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.RicettaRepository;
import jakarta.validation.Valid;

@Controller
public class RicettaController {
	@Autowired 
	private RicettaRepository ricettaRepository;
	
	@GetMapping(value = "/manageRicette") 
	public String manageRicette (Model model) {
			model.addAttribute("ricette", this.ricettaRepository.findAll());
			return "manageRicette.html";
	}
	
	@GetMapping(value="/formNewRicetta")
	public String formNewMovie(Model model) {
		model.addAttribute("ricetta", new Ricetta());
		return "formNewRicetta.html";
	}
	
	@PostMapping("/ricetta")
	public String newRicetta(@Valid @ModelAttribute("ricetta") Ricetta ricetta, BindingResult bindingResult, Model model) {
		
		
		if (!bindingResult.hasErrors()) {
			//ricetta.setAuthor(null);DA IMPLEMENTARE! PASSERO' UTENTE ATTIVO
			this.ricettaRepository.save(ricetta); 
			model.addAttribute("ricetta", ricetta);
			return "ricetta.html";
		} else {
			return "formNewRicetta.html"; 
		}
	}

}
