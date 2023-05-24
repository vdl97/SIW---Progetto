package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ingrediente_ricetta_porzione")
@IdClass(IngredienteRicettaPorzione.class)
public class IngredienteRicettaPorzione {
	@Id
    @ManyToOne
    @JoinColumn(name = "ricetta_id", referencedColumnName = "id")
    private Ricetta ricetta;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "ingrediente_id", referencedColumnName = "id")
    private Ingrediente ingrediente;
    
    private String porzione;

	public Ricetta getRicetta() {
		return ricetta;
	}

	public void setRicetta(Ricetta ricetta) {
		this.ricetta = ricetta;
	}

	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Ingrediente ingrediente) {
		this.ingrediente = ingrediente;
	}

	public String getPorzione() {
		return porzione;
	}

	public void setPorzione(String porzione) {
		this.porzione = porzione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ingrediente, ricetta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IngredienteRicettaPorzione other = (IngredienteRicettaPorzione) obj;
		return Objects.equals(ingrediente, other.ingrediente) && Objects.equals(ricetta, other.ricetta);
	}
}
