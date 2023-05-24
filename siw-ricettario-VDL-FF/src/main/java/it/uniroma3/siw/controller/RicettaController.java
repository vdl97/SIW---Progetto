package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.RicettaRepository;
import jakarta.validation.Valid;

@Controller
public class RicettaController {
	@Autowired
	private RicettaRepository ricettaRepository;
	@Autowired
	private IngredienteRepository ingredienteRepository;

	@GetMapping(value = "/manageRicette")
	public String manageRicette(Model model) {
		model.addAttribute("ricette", this.ricettaRepository.findAll());
		return "manageRicette.html";
	}

	// POSSIBILE FORMNEWRICETTA+ PASSANDO DATI DI RICETTA INSERITIT E MODIFICANDO LA
	// LISTA DI INGREDIENTI, RIMUOVENDO I GIà SELEZIONATI E MOSTRANDOLI A SCHERMO
	// PASSANDO IL MODEL INGRINSERITI

	@GetMapping(value = "/formNewRicetta")
	public String formNewRicetta(Model model) {

		List<Ingrediente> ingredienti = (List<Ingrediente>) this.ingredienteRepository.findAll();
		model.addAttribute("ingredienti", ingredienti);

		model.addAttribute("ricetta", new Ricetta());

		return "formNewRicetta.html";
	}

	@PostMapping("/ricetta")
	public String newRicetta(@Valid @ModelAttribute("ricetta") Ricetta ricetta, BindingResult bindingResult,
			Model model) {

		if (!bindingResult.hasErrors()) {
			// ricetta.setAuthor(null);DA IMPLEMENTARE! PASSERO' UTENTE ATTIVO
			this.ricettaRepository.save(ricetta);
			model.addAttribute("ricetta", ricetta);
			return "ricetta.html";
		} else {
			return "formNewRicetta.html";
		}
	}

	@GetMapping("/ricetta/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ricetta", this.ricettaRepository.findById(id).get());
		return "ricetta.html";
	}

	@GetMapping("/ricette")
	public String getMovies(Model model) {

//    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		model.addAttribute("ricette", this.ricettaRepository.findAll());
//		model.addAttribute("user", credentials.getUser());
		return "ricette.html";

	}

	@GetMapping("/formSearchRicetta")
	public String formSearchRicetta() {
		return "formSearchRicetta.html";
	}

	@PostMapping("/searchRicette")
	public String searchRicette(Model model, @RequestParam String title) {
		model.addAttribute("ricette", this.ricettaRepository.findByTitle(title));
		return "foundRicette.html";
	}

}