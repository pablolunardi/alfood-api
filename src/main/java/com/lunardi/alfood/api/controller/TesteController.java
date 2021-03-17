package com.lunardi.alfood.api.controller;

import static com.lunardi.alfood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.lunardi.alfood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunardi.alfood.domain.model.Restaurante;
import com.lunardi.alfood.domain.repository.RestauranteRepository;

@RestController
public class TesteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@GetMapping("/restaurantes/por-nome-e-frete")
	public ResponseEntity<List<Restaurante>> listaPorNomeEFrete(String nome,
			BigDecimal taxaInicial, BigDecimal taxaFinal) {
		return ResponseEntity.ok(restauranteRepository.find(nome, taxaInicial, taxaFinal));
	}
	
	@GetMapping("/restaurantes/com-frete-gratis")
	public List<Restaurante> restaurantesComFreteGratis(String nome) {
		return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
	}

}
