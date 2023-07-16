package it.uniroma3.siw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

	// @NotBlank
	private String descriptionRicetta;

	@NotBlank
	private String portata;
	
	private Boolean isValidated;

	@OneToMany(mappedBy = "ricetta", cascade = CascadeType.REMOVE)
	private Set<RicettaLine> ricettaLines;

	@ManyToOne
	private Credentials author;

	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Picture> pictures;

	@OneToMany(mappedBy = "relatedRicetta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Review> reviews;


	public Ricetta() {
		this.ricettaLines = new HashSet<>();
	}

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

	public Set<RicettaLine> getRicettaLines() {
		return ricettaLines;
	}

	public void setRicettaLines(Set<RicettaLine> ricettaLines) {
		this.ricettaLines = ricettaLines;
	}

	public Credentials getAuthor() {
		return author;
	}

	public void setAuthor(Credentials author) {
		this.author = author;
	}

	public String getPortata() {
		return portata;
	}

	public void setPortata(String portata) {
		this.portata = portata;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Boolean getIsValidated() {
		return isValidated;
	}

	public void setIsValidated(Boolean isValidated) {
		this.isValidated = isValidated;
	}

}
