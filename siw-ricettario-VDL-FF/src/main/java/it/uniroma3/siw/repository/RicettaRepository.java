package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.Utente;

public interface RicettaRepository extends CrudRepository<Ricetta,Long>{
	
	public List<Ricetta> findByTitle(String title);
	
	//public boolean existByTitle(String title);
	
	//public boolean existsByUserAndTitle(User user, String title);
}
