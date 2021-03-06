package com.lunardi.alfood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunardi.alfood.api.assembler.RestauranteInputDisassembler;
import com.lunardi.alfood.api.assembler.RestauranteModelAssembler;
import com.lunardi.alfood.api.model.RestauranteModel;
import com.lunardi.alfood.api.model.input.CozinhaIdInput;
import com.lunardi.alfood.api.model.input.RestauranteInput;
import com.lunardi.alfood.core.validation.ValidacaoException;
import com.lunardi.alfood.domain.exception.CozinhaNaoEncontradaException;
import com.lunardi.alfood.domain.exception.NegocioException;
import com.lunardi.alfood.domain.model.Restaurante;
import com.lunardi.alfood.domain.repository.RestauranteRepository;
import com.lunardi.alfood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@Autowired
	private SmartValidator validator;
	
	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}
	
	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		return restauranteModelAssembler.toModel(restaurante);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteInputDisassembler.toDomainObject(restauranteInput)));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
	//	Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
		
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
//		BeanUtils.copyProperties(restaurante, restauranteAtual, 
//				"id", "formasPagamento", "endereco", "dataCadastro", "produtos");

		restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
		
		try {
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@PatchMapping("/{restauranteId}")
	public RestauranteModel atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos, HttpServletRequest request) {
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		merge(campos, restauranteAtual, request);
		validate(toInputObject(restauranteAtual), "restauranteInput");
		
		return atualizar(restauranteId, toInputObject(restauranteAtual));
	}

	private void validate(RestauranteInput restauranteAtual, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restauranteAtual, objectName);
		
		validator.validate(restauranteAtual, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
		
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
			
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	
	private RestauranteInput toInputObject(Restaurante restaurante) {
		RestauranteInput restauranteInput = new RestauranteInput();
		restauranteInput.setNome(restaurante.getNome());
		restauranteInput.setTaxaFrete(restaurante.getTaxaFrete());
		
		CozinhaIdInput cozinha = new CozinhaIdInput();
		cozinha.setId(restaurante.getCozinha().getId());
		
		restauranteInput.setCozinha(cozinha);
		
		return restauranteInput;
	}
	
}
