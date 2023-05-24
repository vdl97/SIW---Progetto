package it.uniroma3.siw.model;





import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
	@Column(columnDefinition="TEXT",nullable=false)
	private String descriptionRicetta;
//	@Column(nullable=false)
//	private int preparationTime;
//	private Integer dosesPerPerson;
//	@ManyToOne
//	private Credentials author;

//IMPLEMENTO RELAZIONE TRA INGREDIENTE E RICETTA USANDO UNA TABELLA INTERMEDIA PORZIONAMENTO (INGR_ID,RIC_ID,QTY)
	@OneToMany(mappedBy = "ricetta")
    private List<IngredienteRicettaPorzione> ingrRicPorz;	
	
	
//PER ORA PROVO AGGIUNGENDO UNA TABELLA INTERMEDIA PORZIONI	
//	@ManyToMany
//	@ElementCollection
//    @CollectionTable(name = "ricetta_ingrediente",
//        joinColumns = @JoinColumn(name = "ricetta_id"))
//    @MapKeyJoinColumn(name = "ingrediente_id")
//    @Column(name = "nome_ingrediente")
//	private Map<Ingrediente,String> listaIngredienti;
	
	private String portata;
	
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

	

@Override
	public int hashCode() {
		return Objects.hash(descriptionRicetta, id, portata, title);
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
				&& Objects.equals(portata, other.portata) && Objects.equals(title, other.title);
	}
	public String getPortata() {
		return portata;
	}
	public void setPortata(String portata) {
		this.portata = portata;
	}
	public List<IngredienteRicettaPorzione> getIngrRicPorz() {
		return ingrRicPorz;
	}
	public void setIngrRicPorz(List<IngredienteRicettaPorzione> ingrRicPorz) {
		this.ingrRicPorz = ingrRicPorz;
	}

}
