package com.exemplo.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TAB_MAQUINA")
public class ComunicacaoMaquina {

	private Long id;
	private String estado;
	private Date data;
	private String sirene;
	
	public ComunicacaoMaquina() {
		
	}
	
	public ComunicacaoMaquina(Long id,String estado, Date data,String sirene) {
		this.id = id;
		this.estado = estado;
		this.data = data;
		this.sirene = sirene;
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ALARME")
	public Long getId() {
		return id;
	}

	

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DETECCAO", nullable = false, length = 100 )
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	

	@Column(name = "DATA_HORA")
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	
	@Column(name = "SIRENE", nullable = false, length = 100 )
	public String getSirene() {
		return sirene;
	}

	public void setSirene(String sirene) {
		this.sirene = sirene;
	}
	
	
	
}