package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Picture;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.PictureRepository;
import it.uniroma3.siw.repository.RicettaRepository;
import javax.validation.Valid;

@Controller
public class RicettaController {
	@Autowired
	private RicettaRepository ricettaRepository;
	@Autowired
	private IngredienteRepository ingredienteRepository;
	@Autowired
	private PictureRepository pictureRepository;

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
	public String newRicetta(@Valid @ModelAttribute("ricetta") Ricetta ricetta,@RequestParam("file") MultipartFile[] files, BindingResult bindingResult,Model model) throws IOException{

		if (!bindingResult.hasErrors()) {
			// ricetta.setAuthor(null);DA IMPLEMENTARE! PASSERO' UTENTE ATTIVO
			ricetta.setPictures(new HashSet<Picture>());
            Picture[] pictures = this.savePictureIfNotExistsOrRetrieve(files);
            for(Picture p:pictures) {
                ricetta.getPictures().add(p);
            }
			this.ricettaRepository.save(ricetta);
			model.addAttribute("ricetta", ricetta);
			return "ricetta.html";
		} else {
			return "formNewRicetta.html";
		}
	}
	
	private Picture[] savePictureIfNotExistsOrRetrieve(MultipartFile[] files) throws IOException {
        Picture[] pictures = new Picture[files.length];
        int i=0;
        for(MultipartFile f:files) {
            Picture picture;
            picture = new Picture();
            picture.setName(f.getResource().getFilename());
            picture.setData(f.getBytes());
            this.pictureRepository.save(picture);
            pictures[i] = picture;
            i++;    
        }
        return pictures;
    }

	@GetMapping("/ricetta/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ricetta", this.ricettaRepository.findById(id).get());
		return "ricetta.html";
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