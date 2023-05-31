package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.IngredienteRicettaPorzione;

public interface IngredienteRicettaPorzioneRepository extends CrudRepository<IngredienteRicettaPorzione,Long>{
	
	@Query(value="select * "
			+ "from ingrediente i "
			+ "where i.id not in "
			+ "(select ingrediente_id "
			+ "from ingrediente_ricetta_porzione "
			+ "where ingrediente_ricetta_porzione.ricetta_id = :ricettaId)", nativeQuery=true)
	public Iterable<Ingrediente> findIngredientiNotInRicetta(@Param("ricettaId") Long id);
}
