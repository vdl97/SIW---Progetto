package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Ingrediente {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@Column(columnDefinition="TEXT",nullable=false)
	private String name;
	private String category;
	private Integer calories;
	
	//IMPLEMENTO RELAZIONE TRA INGREDIENTE E RICETTA USANDO UNA TABELLA INTERMEDIA PORZIONAMENTO (INGR_ID,RIC_ID,QTY)
	 @OneToMany(mappedBy = "ingrediente")
	    private List<IngredienteRicettaPorzione> ingrRicPorz;
	
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
		this.category= descriptionProduct;
	}
	
	public Integer getCalories() {
		return calories;
	}
	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(calories, category, id, name);
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
		return Objects.equals(calories, other.calories) && Objects.equals(category, other.category)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
}
