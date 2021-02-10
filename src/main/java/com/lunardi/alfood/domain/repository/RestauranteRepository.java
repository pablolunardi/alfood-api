package com.lunardi.alfood.domain.repository;

import java.util.List;

import com.lunardi.alfood.domain.model.Restaurante;

public interface RestauranteRepository {

	List<Restaurante> listar();
	
	Restaurante salvar(Restaurante restaurante);
	
	Restaurante buscar(Long id);
	
	void remover(Restaurante restaurante);
	
}
