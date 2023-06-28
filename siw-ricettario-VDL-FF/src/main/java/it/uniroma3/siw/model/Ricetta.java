package it.uniroma3.siw.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ricetta {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	private String descriptionRicetta;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "orders_id")
	private List<RicettaLine> ricettaLines;

	@ManyToOne
	private Credentials author;

	@ManyToMany
	private Set<Picture> pictures;

//	private String portata;

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

	public String getDescriptionRicetta() {
		return descriptionRicetta;
	}

	public void setDescriptionRicetta(String descriptionRicetta) {
		this.descriptionRicetta = descriptionRicetta;
	}

	public Set<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(Set<Picture> pictures) {
		this.pictures = pictures;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descriptionRicetta, id, title);
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
		return Objects.equals(descriptionRicetta, other.descriptionRicetta) && Objects.equals(id, other.id)
			 && Objects.equals(title, other.title);
	}


}
