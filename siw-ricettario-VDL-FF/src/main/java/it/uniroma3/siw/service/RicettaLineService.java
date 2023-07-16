package it.uniroma3.siw.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.RicettaLine;
import it.uniroma3.siw.repository.RicettaLineRepository;

@Service
public class RicettaLineService {

	@Autowired
	private RicettaLineRepository ricettaLineRepository;
	@Autowired
	private RicettaService ricettaService;
	@Autowired
	private IngredienteService ingredienteService;

	@Transactional
	public boolean addNewLineToRicettaLines(Long ricettaId, Long ingredienteId) {
		boolean res = false;
		Ricetta ricetta = this.ricettaService.findById(ricettaId);
		Ingrediente ingrediente = this.ingredienteService.findById(ingredienteId);

		if (ricetta == null || ingrediente == null)
			return res;
		else {
			RicettaLine ricettaLine = new RicettaLine();
			ricettaLine.setIngrediente(ingrediente);
			ricettaLine.setRicetta(ricetta);
			Set<RicettaLine> ricettaLines = ricetta.getRicettaLines();
			ricettaLines.add(ricettaLine);
			this.ricettaLineRepository.save(ricettaLine);
			this.ricettaService.save(ricetta);
			res = true;
		}
		return res;
	}

	@Transactional
	public boolean removeLineToRicettaLines(Long ricettaId, Long ingredienteId) {
		boolean res = false;
		Ricetta ricetta = this.ricettaService.findById(ricettaId);
		Ingrediente ingrediente = this.ingredienteService.findById(ingredienteId);

		if (ricetta == null || ingrediente == null)
			return res;
		else {
			this.ricettaLineRepository.removeRicettaLineForIngredienteAndRicetta(ingrediente,ricetta);
			this.ricettaService.save(ricetta);
			res = true;
			return res;
		}
	}

	@Transactional
	private void removeAndUpdateRicettaLinesForRicetta(RicettaLine i, Ricetta ricetta) {
		ricetta.getRicettaLines().remove(i);
		this.ricettaService.save(ricetta);
	}
}
