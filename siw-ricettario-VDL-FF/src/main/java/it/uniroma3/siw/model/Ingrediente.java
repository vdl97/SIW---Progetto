package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ingrediente {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@Column(columnDefinition="TEXT",nullable=false)
	private String name;
	private String descriptionProduct;
	private Integer calories;
	private String fonte;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescriptionProduct() {
		return this.descriptionProduct;
	}
	public void setDescriptionProduct(String descriptionProduct) {
		this.descriptionProduct= descriptionProduct;
	}
	
	public Integer getCalories() {
		return calories;
	}
	public void setCalories(Integer calories) {
		this.calories = calories;
	}
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(calories, descriptionProduct, fonte, id, name);
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
		return Objects.equals(calories, other.calories) && Objects.equals(descriptionProduct, other.descriptionProduct)
				&& Objects.equals(fonte, other.fonte) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name);
	}
}
