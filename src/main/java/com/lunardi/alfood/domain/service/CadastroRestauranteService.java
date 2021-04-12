package com.lunardi.alfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lunardi.alfood.domain.exception.RestauranteNaoEncontradoException;
import com.lunardi.alfood.domain.model.Cozinha;
import com.lunardi.alfood.domain.model.Restaurante;
import com.lunardi.alfood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();

		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);

		restaurante.setCozinha(cozinha);

		return restauranteRepository.save(restaurante);
	}

	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId).orElseThrow(
				() -> new RestauranteNaoEncontradoException(restauranteId));
	}

}
