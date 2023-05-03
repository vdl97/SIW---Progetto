package it.uniroma3.siw.model;





import java.util.Date;
import java.util.List;
import java.util.Objects;

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
	private Date time;
	private Integer dosesPerPerson;
	@Column(columnDefinition="TEXT")
	private String descriptionRicetta;
	
	//GESTIONE ONE TO MANY/MANY TO ONE
	private List<Ingrediente> listaIngredienti;
	
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getDosesPerPerson() {
		return dosesPerPerson;
	}
	public void setDosesPerPerson(int dosesPerPerson) {
		this.dosesPerPerson = dosesPerPerson;
	}
	public List<Ingrediente> getListaIngredienti() {
		return listaIngredienti;
	}
	public void setListaIngredienti(List<Ingrediente> listaIngredienti) {
		this.listaIngredienti = listaIngredienti;
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
}
