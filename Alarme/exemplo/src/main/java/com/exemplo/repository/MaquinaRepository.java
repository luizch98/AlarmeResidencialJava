package com.exemplo.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


import com.exemplo.model.ComunicacaoMaquina;

public class MaquinaRepository extends GenericRepository<ComunicacaoMaquina, Long>{

	public MaquinaRepository(EntityManager entityManager) {
		super(entityManager);
	}

	
	public List<ComunicacaoMaquina> listarTodosRegistrosComPaginacao(Integer primeiro, Integer tamanhoPagina){
		List<ComunicacaoMaquina> listaMaquina = new ArrayList<ComunicacaoMaquina>();
		try {
			TypedQuery<ComunicacaoMaquina> query = this.getEntityManager()
			                                .createQuery("SELECT c FROM Exemplo c", ComunicacaoMaquina.class)
											.setFirstResult(primeiro)
											.setMaxResults(tamanhoPagina);
		listaMaquina = query.getResultList();
		} catch( Exception e) {
			e.printStackTrace();
		}

		return listaMaquina;
	}
	
}