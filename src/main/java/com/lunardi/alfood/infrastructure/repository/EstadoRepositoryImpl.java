package com.lunardi.alfood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lunardi.alfood.domain.model.Estado;
import com.lunardi.alfood.domain.repository.EstadoRepository;

//@Component
public class EstadoRepositoryImpl   {

	@PersistenceContext
	private EntityManager manager;

	//@Override
	public List<Estado> listar() {
		return manager.createQuery("from Estado", Estado.class).getResultList();
	}

	//@Override
	public Estado buscar(Long id) {
		return manager.find(Estado.class, id);
	}

	@Transactional
	//@Override
	public Estado salvar(Estado estado) {
		return manager.merge(estado);
	}

	@Transactional
	//@Override
	public void remover(Long id) {
		Estado estado = buscar(id);

		if (estado == null) {
			throw new EmptyResultDataAccessException(1);
		}

		manager.remove(estado);
	}

}
