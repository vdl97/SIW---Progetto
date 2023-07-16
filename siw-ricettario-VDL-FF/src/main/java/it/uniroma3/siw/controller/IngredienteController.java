package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.PictureRepository;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.PictureService;

@Controller
public class IngredienteController {
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private PictureService pictureService;

	public static final List<String> CATEGORY_TYPE = new ArrayList<>(
			Arrays.asList("Gin", "Vermouth", "Vodka", "Rum", "Bitter", "Tequila", "Whiskey", "Sodato","Altro"));

	@GetMapping("/ingrediente/{id}")
	public String getIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "ingrediente.html";
	}

	@GetMapping("/ingredienti")
	public String getIngredienti(Model model) {
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "ingredienti.html";
	}

	@GetMapping("/admin/formNewIngrediente")
	public String formNewIngrediente(Model model) {
		model.addAttribute("categories", CATEGORY_TYPE);
		model.addAttribute("ingrediente", new Ingrediente());
		return "admin/formNewIngrediente.html";
	}

	@PostMapping("/formIngrediente")
	public String newIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente,
			BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile[] files) throws IOException {
		
		//if (!ingredienteService.existsByNameAndCategory(ingrediente.getName(), ingrediente.getCategory())) {
		if(!bindingResult.hasErrors()) {
			this.ingredienteService.saveCreatedNewIngrediente(ingrediente, files);

			model.addAttribute("ingrediente", ingrediente);
			return "ingrediente.html";
		} else {
			model.addAttribute("categories", CATEGORY_TYPE);
			model.addAttribute("ingrediente", ingrediente);
			return "admin/formNewIngrediente.html";
		}
	}
	

	@GetMapping(value="/admin/indexIngrediente")
	public String indexIngrediente() {
		return "admin/indexIngrediente.html";
	}
	

	@GetMapping("/formSearchIngrediente")
	public String formSearchIngrediente() {
		return "formSearchIngrediente.html";
	}

	@PostMapping("/searchIngrediente")
	public String searchIngredienti(Model model, @RequestParam float contentMin,@RequestParam float contentMax) {
		model.addAttribute("ingredienti", this.ingredienteService.findByIngredienteAlcoholContent(contentMin,contentMax));
		return "foundIngredienti.html";
	}
	@GetMapping(value = "/admin/manageIngredienti")
	public String manageIngredienti(Model model) {
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "admin/manageIngredienti.html";
	}
	
	@GetMapping(value = "/admin/UpdateIngrediente/{id}")
	public String UpdateIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		if (ingrediente==null)
			return "IngredienteError.html";
		else
			model.addAttribute("ingrediente", ingrediente);
		 
		return "admin/UpdateIngrediente.html";
	}

	@PostMapping("/admin/updateIngrediente/{id}")
	public String updateIngrediente(@PathVariable("id") Long id, @Valid @ModelAttribute("ingrediente") Ingrediente ingrediente,
			BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile[] files) throws IOException {

		if (!bindingResult.hasErrors()) {
			ingrediente.setId(id);
			this.ingredienteService.saveUpdatedIngrediente(ingrediente, id);
			ingrediente = this.ingredienteService.findById(id);
			this.ingredienteService.addPicturesToIngrediente(id, files);

			model.addAttribute("ingrediente", this.ingredienteService.findById(id));
	
			
			return "ingrediente.html";

		} else {
			model.addAttribute("ingrediente", ingrediente);
			return "admin/formUpdateIngrediente.html";
		}
	}

}
