package com.lunardi.alfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.lunardi.alfood.domain.exception.EntidadeNaoEncontradaException;
import com.lunardi.alfood.domain.model.Cozinha;
import com.lunardi.alfood.domain.model.Restaurante;
import com.lunardi.alfood.domain.repository.CozinhaRepository;
import com.lunardi.alfood.domain.repository.RestauranteRepository;

public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);
		
		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastrado de cozinha com o código %d", cozinhaId));
		}
		
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.salvar(restaurante);
	}
	
}
