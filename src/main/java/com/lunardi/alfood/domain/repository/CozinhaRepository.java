package com.lunardi.alfood.domain.repository;

import java.util.List;

import com.lunardi.alfood.domain.model.Cozinha;

public interface CozinhaRepository {

	List<Cozinha> listar();

	Cozinha buscar(Long id);

	Cozinha salvar(Cozinha cozinha);

	void remover(Cozinha xozinha);

}
