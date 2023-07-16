package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Ingrediente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(columnDefinition = "TEXT", nullable = false)
	private String name;
	
	private String category;
	
	private float alcoholContent;

	@ManyToMany
	private Set<Picture> pictures;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptionProduct() {
		return this.category;
	}

	public void setDescriptionProduct(String descriptionProduct) {
		this.category = descriptionProduct;
	}

	public float getCalories() {
		return alcoholContent;
	}

	public void setCalories(Integer calories) {
		this.alcoholContent = calories;
	}

	@Override
	public int hashCode() {
		return Objects.hash(alcoholContent, category, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingrediente other = (Ingrediente) obj;
		return Objects.equals(alcoholContent, other.alcoholContent) && Objects.equals(category, other.category)
				&& Objects.equals(name, other.name);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Set<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(Set<Picture> pictures) {
		this.pictures = pictures;
	}

	public float getAlcoholContent() {
		return alcoholContent;
	}

	public void setAlcoholContent(float alcoholContent) {
		this.alcoholContent = alcoholContent;
	}



}
