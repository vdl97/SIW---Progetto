package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

	public boolean existsByNameAndCategory(String name, String catgory);

	@Query(value = "select * " + "from ingrediente i " + "where i.id not in " + "(select ingrediente_id "
			+ "from ricetta_line where " + "ricetta_line.ricetta_id=:ricettaId)", nativeQuery = true)
	public Iterable<Ingrediente> findIngredientiNotInRicetta(@Param("ricettaId") Long idR);

	@Query(value = "select * " + "from ingrediente i " + "where i.id in " + "(select ingrediente_id "
			+ "from ricetta_line where " + "ricetta_line.ricetta_id=:ricettaId)", nativeQuery = true)
	public Iterable<Ingrediente> findIngredientiInRicetta(@Param("ricettaId") Long idR);

	@Query(value = "select * from ingrediente i where i.alcohol_content>:contentMin and i.alcohol_content<:contentMax", nativeQuery = true)
	public Set<Ingrediente> findByIngredienteAlcoholContent(@Param("contentMin") float contentMin,
			@Param("contentMax") float contentMax);

	@Modifying
	@Query(value="update Ingrediente set "
			+ "name = :name, category= :category , alcohol_content=:alcohol_content "
			+ "where id = :id", nativeQuery = true)
	int updateIngredienteInfo(@Param("name") String name, @Param("category") String category,
			@Param("alcohol_content") float alcohol_content, @Param("id") Long id);
}
