package com.exemplo.service;

import java.util.List;

import javax.persistence.EntityTransaction;
import com.exemplo.repository.MaquinaRepository;
import com.exemplo.model.ComunicacaoMaquina;

public class MaquinaService extends ConexaoBancoService{

	private MaquinaRepository maquinaRepository;
	
	private static Integer ERRO_INCLUSAO = 1;
	private static Integer ERRO_ALTERACAO = 2;
	private static Integer ERRO_EXCLUSAO = 3;
	private static Integer SUCESSO_TRANSACAO = 0;
	
	public MaquinaService() {
		maquinaRepository = new MaquinaRepository(getEntityManager());
	}
	
		
	public Integer salvarMaquina(ComunicacaoMaquina maquina) {
		EntityTransaction transacao = this.getEntityManager().getTransaction();
		try {
			transacao.begin();
			maquinaRepository.save(maquina);
			transacao.commit();
		}catch (Throwable e) {
			e.printStackTrace();
	        if (transacao.isActive()) {
	        	transacao.rollback();
	        	close();
	        }
			return ERRO_INCLUSAO;
		} finally {
			close();
		}
		return SUCESSO_TRANSACAO;
	}
	

	public Integer alterarMaquina(ComunicacaoMaquina maquina) {
		EntityTransaction transacao = this.getEntityManager().getTransaction();
		try {
			transacao.begin();
			maquinaRepository.update(maquina);
			transacao.commit();
		} catch(Throwable e) {
			e.printStackTrace();
			if (transacao.isActive()) {
				transacao.rollback();
				close();
			}
			return ERRO_ALTERACAO;
		} finally {
			close();
		}
		return SUCESSO_TRANSACAO;
	}
	
	public Integer excluirMaquina(ComunicacaoMaquina maquina) {
		EntityTransaction transacao = this.getEntityManager().getTransaction();
		try {
			ComunicacaoMaquina maquinaRemovido = consultarMaquina(maquina.getId());
			transacao.begin();
			maquinaRepository.remove(maquinaRemovido);
			transacao.commit();
		} catch(Throwable e) {
			e.printStackTrace();
			if (transacao.isActive()) {
				transacao.rollback();
				close();
			}
			return ERRO_EXCLUSAO;
		} finally {
			close();
		}
		return SUCESSO_TRANSACAO;
	}
	
	public ComunicacaoMaquina consultarMaquina(Long id) {
		return maquinaRepository.findById(id);
	}
	
	public List<ComunicacaoMaquina> listarTodosMaquinas(){
		return maquinaRepository.findAll(ComunicacaoMaquina.class);
	}
}
