package com.lunardi.alfood;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.lunardi.alfood.domain.exception.CozinhaNaoEncontradaException;
import com.lunardi.alfood.domain.exception.EntidadeEmUsoException;
import com.lunardi.alfood.domain.model.Cozinha;
import com.lunardi.alfood.domain.model.Restaurante;
import com.lunardi.alfood.domain.repository.CozinhaRepository;
import com.lunardi.alfood.domain.service.CadastroCozinhaService;
import com.lunardi.alfood.domain.service.CadastroRestauranteService;
import com.lunardi.alfood.util.DatabaseCleaner;
import com.lunardi.alfood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIT {

	private static final int COZINHA_ID_INEXISTENTE = 100;

	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";

		databaseCleaner.clearTables();
		prepararDados();
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource("/json/cozinha-chinesa.json");
	}
	
	@Test
	void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		given()
			.pathParam("cozinhaId", cozinhaAmericana.getId())
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(cozinhaAmericana.getNome()));
	}
	
	@Test
	void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		given()
			.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	void deveRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			.body(jsonCorretoCozinhaChinesa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	void deveRetornarStatus200_QuandoConsultarCozinhas() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	void deveConterCozinhas_QuandoConsultarCozinhas() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeCozinhasCadastradas));
	}
 	
	@Test
	void deveCadastrar_QuandoCadastrarNovaCozinha() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		
		novaCozinha = cadastroCozinha.salvar(novaCozinha);
		
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test
	void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);

		Assertions.assertThrows(ConstraintViolationException.class, () -> cadastroCozinha.salvar(novaCozinha));
	}
	
	@Test
	void deveFalhar_QuandoExcluirCozinhaEmUso() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Nova cozinha");
		
		Restaurante novoRestaurante = new Restaurante();
		novoRestaurante.setNome("Novo restaurante");
		novoRestaurante.setCozinha(novaCozinha);
		novoRestaurante.setTaxaFrete(new BigDecimal("12.2"));
		
		novaCozinha = cadastroCozinha.salvar(novaCozinha);
		Long cozinhaId = novaCozinha.getId();
		
		cadastroRestaurante.salvar(novoRestaurante);
		
		Assertions.assertThrows(EntidadeEmUsoException.class, () -> cadastroCozinha.excluir(cozinhaId));
	}
	
	@Test
	void deveFalhar_QuandoExcluirCozinhaInexistente() {
		Long cozinhaId = 20L;
		
		Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> cadastroCozinha.excluir(cozinhaId));
	}
	
	private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);

		cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(cozinhaAmericana);	
		
		quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
	}
	

}
