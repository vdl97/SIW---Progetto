package it.uniroma3.siw.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {

	public boolean existsByAuthor(Credentials author);	
	public boolean existsByAuthorAndRelatedRicetta(Credentials author,Ricetta relatedRicetta);	

	@Query(value=
			" SELECT * FROM Review rev "
			+ "WHERE rev.related_ricetta_id = :related_ricetta_id "
			+ "and rev.author_id= :author_id", nativeQuery=true)
	public List<Review> checkReviewForAuthorAndRelatedRicetta(@Param("related_ricetta_id") Long related_ricetta_id,@Param("author_id") Long author_id);

	@Modifying
	@Query("update Review rev set "
			+ "rev.title = :title, rev.rating= :rating, rev.description=:description "
			+ "where rev.id = :id")
	int updateReviewInfo(@Param("title") String title, @Param("rating")Integer rating,@Param("description") String description, @Param("id")Long id);

	

}