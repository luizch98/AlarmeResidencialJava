package com.exemplo.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.exemplo.persistencia.ConexaoBanco;

public abstract class ConexaoBancoService {

	@PersistenceContext(unitName = "exemplo")
	private EntityManager entityManager;

	public ConexaoBancoService() {
		this.entityManager = ConexaoBanco.getConexaoBanco().getEntityManager();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public void close() {
		if (this.getEntityManager().isOpen()) {
			this.getEntityManager().close();
		}
	}
	
	
}
