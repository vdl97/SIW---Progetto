package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.IterableConfigurationPropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Picture;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.RicettaLine;
import it.uniroma3.siw.repository.RicettaRepository;

@Service
public class RicettaService {
	@Autowired
	private RicettaRepository ricettaRepository;
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private RicettaLineService ricettaLineService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private PictureService pictureService;

	public Iterable<Ricetta> findAll() {
		return this.ricettaRepository.findAll();
	}

	@Transactional
	public void save(@Valid Ricetta ricetta) {
		this.ricettaRepository.save(ricetta);
	}

	public Ricetta findById(Long id) {
		return this.ricettaRepository.findById(id).orElse(null);
	}

	public List<Ricetta> findByTitle(String title) {
		return this.ricettaRepository.findByTitle(title);
	}

	@Transactional
	public boolean addIngredienteToRicetta(Long ricettaId, Long ingredienteId) {
		boolean res = false;

		Ricetta ricetta = this.findById(ricettaId);
		if (ricetta == null)
			return res;
		Set<RicettaLine> ricettaLines = ricetta.getRicettaLines();
		Ingrediente ingrediente = this.ingredienteService.findById(ingredienteId);
		// non inserisco se ing null o se set è popolato --> E gia linea con
		// ingrediente!
		long size = this.ricettaRepository.checkIfIngredienteInRicetta(ingredienteId, ricettaId).spliterator()
				.getExactSizeIfKnown();

		if (ingrediente == null || size != 0)
			return res;
		else {
			// Per ora creo linea senza info su quantità
			res = this.ricettaLineService.addNewLineToRicettaLines(ricettaId, ingredienteId);
		}
		return res;
	}

	@Transactional
	public boolean removeIngredienteFromRicetta(Long ricettaId, Long ingredienteId) {
		boolean res = false;

		Ricetta ricetta = this.findById(ricettaId);
		Ingrediente ingrediente = this.ingredienteService.findById(ingredienteId);
		// non inserisco se ing null o se set è popolato --> E gia linea con
		// ingrediente!
		if (ingrediente == null || ricetta == null)
			return res;

		long size = 1; // this.checkIfIngredienteInRicetta(ricettaId, ingredienteId);

		if (size == 0 || size == -1)
			return res;
		else {
			res = true;
			this.ricettaLineService.removeLineToRicettaLines(ricettaId, ingredienteId);
		}
		return res;
	}

	public Set<RicettaLine> checkIfIngredienteInRicetta(Long ricettaId, Long ingredienteId) {
		return this.ricettaRepository.checkIfIngredienteInRicetta(ingredienteId, ricettaId);
	}

	@Transactional
	public void saveUpdatedRicetta(@Valid Ricetta ricetta, Long id) {

		this.ricettaRepository.updateRicettaInfo(ricetta.getTitle(), ricetta.getPortata(), id);

	}

	public void saveCreatedNewRicetta(@Valid Ricetta ricetta, MultipartFile[] files) throws IOException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		ricetta.setAuthor(credentials);
		//ricetta.setIsValidated(false);
		ricetta.setPictures(new HashSet<Picture>());
		if (files.length == 0)
			this.ricettaRepository.save(ricetta);
		else {
			this.pictureService.setPicturesForRicetta(ricetta, files);
			this.ricettaRepository.save(ricetta);
		}
	}

	public boolean checkIfRicettaBelongToUser(Long id) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (this.findById(id).getAuthor().equals(credentials))
			return true;
		return false;
	}

	@Transactional
	public boolean deletePictureFromRicetta(Long idM, Long idP) {
		boolean res = false;
		Ricetta ricetta = this.findById(idM);
		Picture picture = this.pictureService.findPictureById(idP);
		if (ricetta == null || picture == null)
			return res;
		ricetta.getPictures().remove(picture);
		this.ricettaRepository.save(ricetta);

		res = true;
		return res;
	}

	@Transactional
	public boolean addPicturesToRicetta(Long idM, MultipartFile[] files) throws IOException {
		boolean res = false;
		Ricetta ricetta = this.findById(idM);
		if (files.equals(null) || ricetta == null)
			return res;
		this.pictureService.setPicturesForRicetta(ricetta, files);
		this.ricettaRepository.save(ricetta);
		res = true;
		return res;
	}

	@Transactional
	public void saveUpdatedDescRicetta(@Valid Ricetta ricetta, Long id) {

		this.ricettaRepository.updateRicettaDesc(ricetta.getDescriptionRicetta(), id);

	}

	public Set<Ricetta> findByIngredienteName(String ingredienteName) {
		return this.ricettaRepository.findByIngredienteName(ingredienteName);
	}

	@Transactional
	public boolean deleteRicetta(Long id) {
		boolean res = false;
		Ricetta ricetta = this.ricettaRepository.findById(id).orElse(null);
		if (ricetta == null)
			return res;
		else {
			this.ricettaRepository.delete(ricetta);
			return true;
		}
	}
}
