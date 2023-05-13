package it.uniroma3.siw.model;





import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.ManyToMany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ricetta {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(nullable=false)
	private String title;
	@Column(nullable=false)
	private int preparationTime;
	private Integer dosesPerPerson;
	@Column(columnDefinition="TEXT")
	private String descriptionRicetta;
	
//	@ManyToMany
//	private Set<Ingrediente> listaIngredienti;
//	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getpreparationTime() {
		return preparationTime;
	}
	public void setpreparationTime(int preparationTime) {
		this.preparationTime = preparationTime;
	}
	public int getDosesPerPerson() {
		return dosesPerPerson;
	}
	public void setDosesPerPerson(int dosesPerPerson) {
		this.dosesPerPerson = dosesPerPerson;
	}
	public String getDescriptionRicetta() {
		return descriptionRicetta;
	}
	public void setDescriptionRicetta(String descriptionRicetta) {
		this.descriptionRicetta = descriptionRicetta;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, title);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ricetta other = (Ricetta) obj;
		return Objects.equals(id, other.id) && Objects.equals(title, other.title);
	}
//	public Set<Ingrediente> getListaIngredienti() {
//		return listaIngredienti;
//	}
//	public void setListaIngredienti(Set<Ingrediente> listaIngredienti) {
//		this.listaIngredienti = listaIngredienti;
//	}
}
