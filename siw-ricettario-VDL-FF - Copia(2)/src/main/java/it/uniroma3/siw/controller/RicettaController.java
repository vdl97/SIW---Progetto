package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.RicettaLine;
import it.uniroma3.siw.controller.validator.RicettaValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.Picture;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.PictureRepository;
import it.uniroma3.siw.repository.RicettaRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.PictureService;
import it.uniroma3.siw.service.RicettaService;

import javax.validation.Valid;

@Controller
public class RicettaController {
	@Autowired
	private RicettaService ricettaService;
	@Autowired
	private RicettaValidator ricettaValidator;
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private CredentialsService credentialsService;

	public static final List<String> PORTATA_TYPE = new ArrayList<>(
			Arrays.asList("Aperitivo", "After Dinner", "Long Drinks"));

	@GetMapping(value = "/ricette")
	public String ricette(Model model) {
		Iterable<Ricetta> ricette = this.ricettaService.findAll();
		model.addAttribute("ricette", ricette);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {

			return "ricette.html";
		} else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				model.addAttribute("role", credentials.getRole());
				return "admin/ricette.html";
			}
			return "default/ricette.html";
		}
	}

	@GetMapping(value = "/default/formNewRicetta")
	public String formNewRicetta(Model model) {
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		model.addAttribute("portate", PORTATA_TYPE);
		model.addAttribute("ricettaLines", new HashSet<RicettaLine>());
		model.addAttribute("ricettaLine", new RicettaLine());
		model.addAttribute("ricetta", new Ricetta());
		return "default/formNewRicetta.html";
	}

	@PostMapping("/ricetta")
	public String newRicetta(@Valid @ModelAttribute("ricetta") Ricetta ricetta, BindingResult bindingResult,
			Model model, @RequestParam("file") MultipartFile[] files) throws IOException {

		this.ricettaValidator.validate(ricetta, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.ricettaService.saveCreatedNewRicetta(ricetta, files);

			model.addAttribute("ricetta", ricetta);
			Iterable<Ingrediente> ingredientiToAdd = this.ingredienteService.findAll();
			model.addAttribute("ingredientiToAdd", ingredientiToAdd);
			return "default/newRicettaRicettaLines.html";
		} else {
			return "default/formNewRicetta.html";
		}
	}

	@GetMapping(value = "/default/newRicettaRicettaLines")
	public String newRicettaRicettaLines(Model model) {

		Iterable<Ingrediente> ingredientiToAdd = this.ingredienteService.findAll();

		model.addAttribute("ingredientiToAdd", ingredientiToAdd);
		model.addAttribute("ricetta", new HashSet<RicettaLine>());

		return "newRicettaLines.html";
	}

	@GetMapping(value = "/addIngredienteToRicetta/{ingredienteId}/{ricettaId}")
	public String addIngredienteToRicetta(@PathVariable("ingredienteId") Long ingredienteId,
			@PathVariable("ricettaId") Long ricettaId, Model model) {

		if (this.ricettaService.addIngredienteToRicetta(ricettaId, ingredienteId)) {

			List<Ingrediente> ingredientiToAdd = (List<Ingrediente>) this.ingredienteService
					.ingredientiToAdd(ricettaId);
			List<Ingrediente> ingredientiInRicetta = (List<Ingrediente>) this.ingredienteService
					.ingredientiInRicetta(ricettaId);

			model.addAttribute("ricetta", this.ricettaService.findById(ricettaId));
			model.addAttribute("ingredientiToAdd", ingredientiToAdd);
			model.addAttribute("ingredientiInRicetta", ingredientiInRicetta);
		} else
			return "index.html";

		return "default/newRicettaRicettaLines.html";
	}

	@GetMapping("/ricetta/{id}")
	public String getRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta = this.ricettaService.findById(id);
		if (ricetta != null) {
			model.addAttribute("ricetta", ricetta);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "ricetta.html";
			} else {
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
				String role = credentials.getRole();
				if (role.equals(Credentials.ADMIN_ROLE)) {
					model.addAttribute("role", role);
					return "ricetta.html";
				}
			}
		} else
			return "RicettaError.html";
		return "ricetta.html";
	}

	@GetMapping(value = "/removeIngredienteFromRicetta/{ingredienteId}/{ricettaId}")
	public String removeIngredienteFromRicetta(@PathVariable("ingredienteId") Long ingredienteId,
			@PathVariable("ricettaId") Long ricettaId, Model model) {

		if (this.ricettaService.removeIngredienteFromRicetta(ricettaId, ingredienteId)) {

			List<Ingrediente> ingredientiToAdd = (List<Ingrediente>) this.ingredienteService
					.ingredientiToAdd(ricettaId);
			List<Ingrediente> ingredientiInRicetta = (List<Ingrediente>) this.ingredienteService
					.ingredientiInRicetta(ricettaId);

			model.addAttribute("ricetta", this.ricettaService.findById(ricettaId));
			model.addAttribute("ingredientiToAdd", ingredientiToAdd);
			model.addAttribute("ingredientiInRicetta", ingredientiInRicetta);
		} else
			return "index.html";

		return "default/newRicettaRicettaLines.html";
	}

	@GetMapping("/formSearchRicetta")
	public String formSearchRicetta() {
		return "formSearchRicetta.html";
	}

	@PostMapping("/searchRicette")
	public String searchRicette(Model model, @RequestParam String ingredienteName) {
		model.addAttribute("ricette", this.ricettaService.findByIngredienteName(ingredienteName));
		return "foundRicette.html";
	}

	@GetMapping(value = "/admin/indexRicetta")
	public String indexRicetta() {
		return "admin/indexRicetta.html";
	}

	@GetMapping(value = "/admin/manageRicette")
	public String manageRicette(Model model) {
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "admin/manageRicette.html";
	}

	@GetMapping(value = "/default/UpdateRicetta/{id}")
	public String UpdateRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta = this.ricettaService.findById(id);
		if (!this.ricettaService.checkIfRicettaBelongToUser(id))
			return "RicettaError.html";
		if (ricetta != null) {
			model.addAttribute("portate", PORTATA_TYPE);
			model.addAttribute("ricetta", ricetta);
		} else
			return "RicettaError.html";
		return "default/UpdateRicetta.html";
	}

	@PostMapping("/default/updateRicetta/{id}")
	public String updateRicetta(@PathVariable("id") Long id, @Valid @ModelAttribute("ricetta") Ricetta ricetta,
			BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile[] files) throws IOException {

		if (!bindingResult.hasErrors()) {
			ricetta.setId(id);
			this.ricettaService.saveUpdatedRicetta(ricetta, id);
			ricetta = this.ricettaService.findById(id);
			List<Ingrediente> ingredientiToAdd = (List<Ingrediente>) this.ingredienteService.ingredientiToAdd(id);
			List<Ingrediente> ingredientiInRicetta = (List<Ingrediente>) this.ingredienteService
					.ingredientiInRicetta(id);

			this.ricettaService.addPicturesToRicetta(id, files);

			model.addAttribute("ricetta", this.ricettaService.findById(id));
			model.addAttribute("ingredientiToAdd", ingredientiToAdd);
			model.addAttribute("ingredientiInRicetta", ingredientiInRicetta);

			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			String role = credentials.getRole();
			if (role.equals(Credentials.ADMIN_ROLE))
				model.addAttribute("role", role);

			return "default/newRicettaRicettaLines.html";

		} else {
			model.addAttribute("ricetta", ricetta);
			return "admin/formUpdateRicetta.html";
		}
	}

	@GetMapping("/default/deletePictureFromRicetta/{idM}/{idP}")
	public String deletePictureFromRicetta(@PathVariable("idM") Long idM, @PathVariable("idP") Long idP, Model model) {
		if (this.ricettaService.deletePictureFromRicetta(idM, idP)) {
			model.addAttribute("ricetta", this.ricettaService.findById(idM));
			return "default/UpdateRicetta.html";
		}
		return "ricettaError.html";
	}

	@PostMapping("/default/addPictureToRicetta/{idM}")
	public String addPictureToRicetta(@PathVariable("idM") Long idM, Model model,
			@RequestParam("file") MultipartFile[] files) throws IOException {
		if (this.ricettaService.addPicturesToRicetta(idM, files)) {
			model.addAttribute("ricetta", this.ricettaService.findById(idM));
			return "default/UpdateRicetta.html";
		}

		return "ricettaError.html";
	}

	@GetMapping("/default/updateDescRicetta/{id}")
	public String UpdateDescRicetta(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ricetta", this.ricettaService.findById(id));
		return "default/updateDescRicetta.html";
	}

	@PostMapping("/default/updateDescRicetta/{id}")
	public String updateDescRicetta(@PathVariable("id") Long id, @Valid @ModelAttribute("ricetta") Ricetta ricetta,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasFieldErrors("descriptionRicetta"))
			return "ricettaError.html";

		ricetta.setId(id);
		this.ricettaService.saveUpdatedDescRicetta(ricetta, id);
		ricetta = this.ricettaService.findById(id);
		model.addAttribute("ricetta", this.ricettaService.findById(id));
		return "ricetta.html";
	}

	@GetMapping(value = "/default/deleteRicetta/{id}")
	public String deleteRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta = this.ricettaService.findById(id);
		if (ricetta != null)
			model.addAttribute("ricetta", ricetta);
		else
			return "RicettaError.html";
		return "default/deleteRicetta.html";
	}

	@GetMapping("default/confirmDeletionRicetta/{id}")
	public String confirmDeletionRicetta(@PathVariable("id") Long id, Model model) {

		if (this.ricettaService.deleteRicetta(id)) {
			model.addAttribute("ricette", this.ricettaService.findAll());
			return "admin/manageRicette.html";
		} else {
			return "RicettaError.html";
		}
	}
	
	@GetMapping("admin/validateRicetta/{id}")
	public String validateRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta=this.ricettaService.findById(id);
		model.addAttribute("ricetta", ricetta);
		return "admin/validateRicetta.html";
	}
	
	@GetMapping("admin/confirmValidationRicetta/{id}")
	public String confirmValidationRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta=this.ricettaService.findById(id);
		ricetta.setIsValidated(true);
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "admin/manageRicette.html";
	}
	
	@GetMapping("admin/rejectValidationRicetta/{id}")
	public String rejectValidationRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta=this.ricettaService.findById(id);
		ricetta.setIsValidated(false);

		model.addAttribute("ricette", this.ricettaService.findAll());
		return "admin/manageRicette.html";
	}
	
	

}