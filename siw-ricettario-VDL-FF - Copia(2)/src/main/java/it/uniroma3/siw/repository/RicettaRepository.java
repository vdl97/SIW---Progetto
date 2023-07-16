package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.RicettaLine;
import it.uniroma3.siw.model.User;

public interface RicettaRepository extends CrudRepository<Ricetta, Long> {

	public List<Ricetta> findByTitle(String title);

	@Query(value = "SELECT * " + "FROM ricetta_line rl " + "WHERE rl.ricetta_id=:ricettaId "
			+ "and rl.ingrediente_id=:ingredienteId", nativeQuery = true)

	public Set<RicettaLine> checkIfIngredienteInRicetta(@Param("ingredienteId") Long ingredienteId,
			@Param("ricettaId") Long ricettaId);

	@Modifying
	@Query("update Ricetta ric set " + "ric.title = :title, ric.portata= :portata " + "where ric.id = :id")
	int updateRicettaInfo(@Param("title") String title, @Param("portata") String portata, @Param("id") Long id);

	@Modifying
	@Query("update Ricetta ric set " + "ric.descriptionRicetta=:descriptionRicetta " + "where ric.id = :id")
	int updateRicettaDesc(@Param("descriptionRicetta") String descriptionRicetta, @Param("id") Long id);

	public boolean existsByTitle(String title);

	@Query(value="select * from ricetta ric "
			+ "where ric.id in "
			+ "(select rl.ricetta_id from ricetta_line rl "
			+ "where rl.ingrediente_id in "
			+ "(select ing.id from ingrediente ing "
			+ "where ing.name=:name))",nativeQuery=true)
	public Set<Ricetta> findByIngredienteName(@Param("name")String name);

}
// public boolean existByTitle(String title);

// public boolean existsByUserAndTitle(User user, String title);
