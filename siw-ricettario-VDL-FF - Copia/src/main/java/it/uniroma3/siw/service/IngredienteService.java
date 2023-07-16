package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Picture;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.IngredienteRepository;

@Service
public class IngredienteService {
	@Autowired
	private PictureService pictureService;
	@Autowired
	private IngredienteRepository ingredienteRepository;

	public Iterable<Ingrediente> ingredientiToAdd(Long idR) {
		return this.ingredienteRepository.findIngredientiNotInRicetta(idR);
	}

	public Iterable<Ingrediente> ingredientiInRicetta(Long idR) {
		return this.ingredienteRepository.findIngredientiInRicetta(idR);
	}

	public Ingrediente findById(Long ingredienteId) {
		return this.ingredienteRepository.findById(ingredienteId).orElse(null);
	}

	public Iterable<Ingrediente> findAll() {
		return this.ingredienteRepository.findAll();
	}

	public boolean existsByNameAndCategory(String name, String category) {
		return this.ingredienteRepository.existsByNameAndCategory(name, category);
	}

	public void save(@Valid Ingrediente ingrediente) {
		this.ingredienteRepository.save(ingrediente);
	}

	public Set<Ingrediente> findByIngredienteAlcoholContent(float contentMin, float contentMax) {
		
		return this.ingredienteRepository.findByIngredienteAlcoholContent(contentMin,contentMax);
	}
	public void saveCreatedNewIngrediente(@Valid Ingrediente ingrediente, MultipartFile[] files) throws IOException {
		ingrediente.setPictures(new HashSet<Picture>());
		if (files.length == 0)
			this.ingredienteRepository.save(ingrediente);
		else {
			this.pictureService.setPicturesForIngrediente(ingrediente, files);
			this.ingredienteRepository.save(ingrediente);
		}
	}

	@Transactional
	public void saveUpdatedIngrediente(@Valid Ingrediente ingrediente, Long id) {

		this.ingredienteRepository.updateIngredienteInfo(ingrediente.getName(), ingrediente.getCategory(), ingrediente.getAlcoholContent(), id);
		
	}

	@Transactional
	public boolean addPicturesToIngrediente(Long idM, MultipartFile[] files) throws IOException {
		boolean res = false;
		Ingrediente ingrediente = this.findById(idM);
		if (files.equals(null) || ingrediente == null)
			return res;
		this.pictureService.setPicturesForIngrediente(ingrediente, files);
		this.ingredienteRepository.save(ingrediente);
		res = true;
		return res;
	}
}
