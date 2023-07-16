package it.uniroma3.siw.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.RicettaLine;

public interface RicettaLineRepository extends CrudRepository<RicettaLine,Long>{

	public List<RicettaLine> findByRicettaAndIngrediente(Long ricettaId,Long ingredienteId);

	@Modifying
	@Query("delete from RicettaLine rl  "
			+ "where rl.ingrediente=:ingrediente_id and rl.ricetta = :ricetta_id")
	int removeRicettaLineForIngredienteAndRicetta(@Param("ingrediente_id") Ingrediente ingrediente, @Param("ricetta_id") Ricetta ricetta_id);

	
	
}
