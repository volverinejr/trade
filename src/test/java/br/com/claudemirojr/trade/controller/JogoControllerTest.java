package br.com.claudemirojr.trade.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Description;

import br.com.claudemirojr.trade.converter.ModelMaperConverter;
import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;
import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;
import br.com.claudemirojr.trade.dto.JogoDto;
import br.com.claudemirojr.trade.dto.JogoResponseDto;
import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.entity.Equipe;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;
import br.com.claudemirojr.trade.model.repository.EquipeRepository;
import br.com.claudemirojr.trade.model.repository.JogoRepository;
import br.com.claudemirojr.trade.testcontainer.AplicacaoStartTestContainer;
import br.com.claudemirojr.trade.util.AuthUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JogoControllerTest extends AplicacaoStartTestContainer {

	
	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private JogoRepository jogoRepository;
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	private String accessToken;
	private Long jogoId;
	private Campeonato campeonato;
	private Equipe mandante;
	private Equipe visitante;


	@LocalServerPort
	private int port;


	@BeforeAll
	void obterToken() {
		campeonatoRepository.deleteAll();
		
		equipeRepository.deleteAll();
		
		jogoRepository.deleteAll();

		accessToken = AuthUtil.obterToken();

		RestAssured.port = port;
		RestAssured.basePath = "/api/trade";
	}


	@Test
	@Order(1)
	@Description("Criar um novo jogo")
	void create() {
		var campeonatoId = criarCampeonato("Brasileirão série A 2025");
		CampeonatoResponseDto campeonatoResponse = buscarCampeonato(campeonatoId);
		assertNotNull(campeonatoResponse);
		campeonato = ModelMaperConverter.parseObject(campeonatoResponse, Campeonato.class);

		Integer numeroRodada = 1;

		var equipeId = criarEquipe("Bahia");
		EquipeResponseDto eqpMandante = buscarEquipe(equipeId);
		assertNotNull(eqpMandante);
		mandante = ModelMaperConverter.parseObject(eqpMandante, Equipe.class);

		Integer eqpMandantePrimeitoTempoTotalGol = 0;
		Integer eqpMandantePrimeitoTempoTotalEscanteio = 0;
		Integer eqpMandanteSegundoTempoTotalGol = 0;
		Integer eqpMandanteSegundoTempoTotalEscanteio = 0;



		equipeId = criarEquipe("Vicetoria");
		EquipeResponseDto eqpVisitante = buscarEquipe(equipeId);
		assertNotNull(eqpVisitante);
		visitante = ModelMaperConverter.parseObject(eqpVisitante, Equipe.class);

		Integer eqpVisitantePrimeitoTempoTotalGol = 0;
		Integer eqpVisitantePrimeitoTempoTotalEscanteio = 0;
		Integer eqpVisitanteSegundoTempoTotalGol = 0;
		Integer eqpVisitanteSegundoTempoTotalEscanteio = 0;


		jogoId = criarJogo(campeonato, numeroRodada,
				mandante,
				eqpMandantePrimeitoTempoTotalGol,
				eqpMandantePrimeitoTempoTotalEscanteio,
				eqpMandanteSegundoTempoTotalGol,
				eqpMandanteSegundoTempoTotalEscanteio,

				visitante,
				eqpVisitantePrimeitoTempoTotalGol,
				eqpVisitantePrimeitoTempoTotalEscanteio,
				eqpVisitanteSegundoTempoTotalGol,
				eqpVisitanteSegundoTempoTotalEscanteio
				);

		assertNotNull(jogoId);
	}


	@Test
	@Order(2)
	@Description("Buscar jogo criado por ID e validar: Campeonato, Mandante, Visitante")
	void findByAuditId() {
		JogoResponseDto jogo = buscarJogo(jogoId);


		assertEquals(campeonato,  jogo.getCampeonato() );
		assertEquals(mandante,  jogo.getEqpMandante() );
		assertEquals(visitante,  jogo.getEqpVisitante() );
	}


	@Test
	@Order(3)
	@Description("Atualizar os dados do jogo")
	void update() {
		Integer numeroRodada = 1;

		var equipeId = criarEquipe("Fortaleza");
		EquipeResponseDto eqpMandante = buscarEquipe(equipeId);
		assertNotNull(eqpMandante);
		mandante = ModelMaperConverter.parseObject(eqpMandante, Equipe.class);

		Integer eqpMandantePrimeitoTempoTotalGol = 1;
		Integer eqpMandantePrimeitoTempoTotalEscanteio = 1;
		Integer eqpMandanteSegundoTempoTotalGol = 1;
		Integer eqpMandanteSegundoTempoTotalEscanteio = 1;



		equipeId = criarEquipe("Ceará");
		EquipeResponseDto eqpVisitante = buscarEquipe(equipeId);
		assertNotNull(eqpVisitante);
		visitante = ModelMaperConverter.parseObject(eqpVisitante, Equipe.class);

		Integer eqpVisitantePrimeitoTempoTotalGol = 2;
		Integer eqpVisitantePrimeitoTempoTotalEscanteio = 2;
		Integer eqpVisitanteSegundoTempoTotalGol = 2;
		Integer eqpVisitanteSegundoTempoTotalEscanteio = 2;


		atualizarJogo(jogoId,
				campeonato, numeroRodada,
				mandante,
				eqpMandantePrimeitoTempoTotalGol,
				eqpMandantePrimeitoTempoTotalEscanteio,
				eqpMandanteSegundoTempoTotalGol,
				eqpMandanteSegundoTempoTotalEscanteio,

				visitante,
				eqpVisitantePrimeitoTempoTotalGol,
				eqpVisitantePrimeitoTempoTotalEscanteio,
				eqpVisitanteSegundoTempoTotalGol,
				eqpVisitanteSegundoTempoTotalEscanteio
				);

		//Confirmar que foi alterado
		JogoResponseDto jogo = buscarJogo(jogoId);


		assertEquals(mandante.getNome(), jogo.getEqpMandante().getNome());
		assertEquals(visitante.getNome(), jogo.getEqpVisitante().getNome());
		assertEquals(eqpVisitantePrimeitoTempoTotalGol, jogo.getEqpVisitantePrimeitoTempoTotalGol() );
	}


	
	@Test
	@Order(4)
	@Description("Atualizar jogo inexistente")
	void updateJogoInexistente() {
		Integer numeroRodada = 1;

		Integer eqpMandantePrimeitoTempoTotalGol = 1;
		Integer eqpMandantePrimeitoTempoTotalEscanteio = 1;
		Integer eqpMandanteSegundoTempoTotalGol = 1;
		Integer eqpMandanteSegundoTempoTotalEscanteio = 1;

		Integer eqpVisitantePrimeitoTempoTotalGol = 2;
		Integer eqpVisitantePrimeitoTempoTotalEscanteio = 2;
		Integer eqpVisitanteSegundoTempoTotalGol = 2;
		Integer eqpVisitanteSegundoTempoTotalEscanteio = 2;


		int statusCode = atualizarJogoInexistente(100L,
				campeonato, numeroRodada,
				mandante,
				eqpMandantePrimeitoTempoTotalGol,
				eqpMandantePrimeitoTempoTotalEscanteio,
				eqpMandanteSegundoTempoTotalGol,
				eqpMandanteSegundoTempoTotalEscanteio,

				visitante,
				eqpVisitantePrimeitoTempoTotalGol,
				eqpVisitantePrimeitoTempoTotalEscanteio,
				eqpVisitanteSegundoTempoTotalGol,
				eqpVisitanteSegundoTempoTotalEscanteio
				);

		
		assertEquals(404, statusCode);
	}
	
	
	
	
	private Long criarEquipe(String nome) {
		EquipeDto equipeDto = new EquipeDto();
		equipeDto.setNome(nome);

		var equipeId = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(equipeDto)
                .when()
                .post("/equipe/v1")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

		return equipeId;
	}


	private EquipeResponseDto buscarEquipe(Long id) {
		EquipeResponseDto equipeResponseDto = RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
			    .contentType("application/json")
			    .when()
			    .get("/equipe/v1/" + id)
			    .then()
			    .statusCode(200)
			    .extract()
			    .as(EquipeResponseDto.class);


		return equipeResponseDto;
	}


	private Long criarCampeonato(String nome) {
		String descricao = "Principal competição de pontos corridos...";
		Boolean ativo = true;

		CampeonatoDto campeonatoDto = new CampeonatoDto();
		campeonatoDto.setNome(nome);
		campeonatoDto.setDescricao(descricao);
		campeonatoDto.setAtivo(ativo);

		var campeonatoId = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(campeonatoDto)
                .when()
                .post("/campeonato/v1")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

		return campeonatoId;
	}


	private CampeonatoResponseDto buscarCampeonato(Long id) {
		CampeonatoResponseDto campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/" + id)
                .then()
                .statusCode(200)
                .extract()
                .as(CampeonatoResponseDto.class);


		return campeonatoResponseDto;
	}


	private Long criarJogo(
			Campeonato campeonato,
			Integer numeroRodada,

			//===== MANDANTE =====
			Equipe eqpMandante,
			Integer eqpMandantePrimeitoTempoTotalGol,
			Integer eqpMandantePrimeitoTempoTotalEscanteio,
			Integer eqpMandanteSegundoTempoTotalGol,
			Integer eqpMandanteSegundoTempoTotalEscanteio,


			//===== VISITANTE =====
			Equipe eqpVisitante,
			Integer eqpVisitantePrimeitoTempoTotalGol,
			Integer eqpVisitantePrimeitoTempoTotalEscanteio,
			Integer eqpVisitanteSegundoTempoTotalGol,
			Integer eqpVisitanteSegundoTempoTotalEscanteio
			) {
		JogoDto jogoDto = new JogoDto();
		jogoDto.setCampeonato(campeonato);

		jogoDto.setNumeroRodada(numeroRodada);

		jogoDto.setEqpMandante(eqpMandante);
		jogoDto.setEqpMandantePrimeitoTempoTotalGol(eqpMandantePrimeitoTempoTotalGol);
		jogoDto.setEqpMandantePrimeitoTempoTotalEscanteio(eqpMandantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpMandanteSegundoTempoTotalGol(eqpMandantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpMandanteSegundoTempoTotalEscanteio(eqpMandanteSegundoTempoTotalEscanteio);


		jogoDto.setEqpVisitante(eqpVisitante);
		jogoDto.setEqpVisitantePrimeitoTempoTotalGol(eqpVisitantePrimeitoTempoTotalGol);
		jogoDto.setEqpVisitantePrimeitoTempoTotalEscanteio(eqpVisitantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpVisitanteSegundoTempoTotalGol(eqpVisitanteSegundoTempoTotalGol);
		jogoDto.setEqpVisitanteSegundoTempoTotalEscanteio(eqpVisitanteSegundoTempoTotalEscanteio);


		var jogoId = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(jogoDto)
                .when()
                .post("/jogo/v1")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");


		return jogoId;
	}


	private JogoResponseDto buscarJogo(Long id) {
		JogoResponseDto jogoResponseDto = RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
			    .contentType("application/json")
			    .when()
			    .get("/jogo/v1/" + id)
			    .then()
			    .statusCode(200)
			    .extract()
			    .as(JogoResponseDto.class);


		return jogoResponseDto;
	}


	private void atualizarJogo(
			Long id,

			Campeonato campeonato,
			Integer numeroRodada,

			//===== MANDANTE =====
			Equipe eqpMandante,
			Integer eqpMandantePrimeitoTempoTotalGol,
			Integer eqpMandantePrimeitoTempoTotalEscanteio,
			Integer eqpMandanteSegundoTempoTotalGol,
			Integer eqpMandanteSegundoTempoTotalEscanteio,


			//===== VISITANTE =====
			Equipe eqpVisitante,
			Integer eqpVisitantePrimeitoTempoTotalGol,
			Integer eqpVisitantePrimeitoTempoTotalEscanteio,
			Integer eqpVisitanteSegundoTempoTotalGol,
			Integer eqpVisitanteSegundoTempoTotalEscanteio
			) {
		JogoDto jogoDto = new JogoDto();
		jogoDto.setCampeonato(campeonato);

		jogoDto.setNumeroRodada(numeroRodada);

		jogoDto.setEqpMandante(eqpMandante);
		jogoDto.setEqpMandantePrimeitoTempoTotalGol(eqpMandantePrimeitoTempoTotalGol);
		jogoDto.setEqpMandantePrimeitoTempoTotalEscanteio(eqpMandantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpMandanteSegundoTempoTotalGol(eqpMandantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpMandanteSegundoTempoTotalEscanteio(eqpMandanteSegundoTempoTotalEscanteio);


		jogoDto.setEqpVisitante(eqpVisitante);
		jogoDto.setEqpVisitantePrimeitoTempoTotalGol(eqpVisitantePrimeitoTempoTotalGol);
		jogoDto.setEqpVisitantePrimeitoTempoTotalEscanteio(eqpVisitantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpVisitanteSegundoTempoTotalGol(eqpVisitanteSegundoTempoTotalGol);
		jogoDto.setEqpVisitanteSegundoTempoTotalEscanteio(eqpVisitanteSegundoTempoTotalEscanteio);


		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(jogoDto)
                .when()
                .put("/jogo/v1/" + id)
                .then()
                .statusCode(200);
	}
	
	
	private int atualizarJogoInexistente(
			Long id,

			Campeonato campeonato,
			Integer numeroRodada,

			//===== MANDANTE =====
			Equipe eqpMandante,
			Integer eqpMandantePrimeitoTempoTotalGol,
			Integer eqpMandantePrimeitoTempoTotalEscanteio,
			Integer eqpMandanteSegundoTempoTotalGol,
			Integer eqpMandanteSegundoTempoTotalEscanteio,


			//===== VISITANTE =====
			Equipe eqpVisitante,
			Integer eqpVisitantePrimeitoTempoTotalGol,
			Integer eqpVisitantePrimeitoTempoTotalEscanteio,
			Integer eqpVisitanteSegundoTempoTotalGol,
			Integer eqpVisitanteSegundoTempoTotalEscanteio
			) {
		JogoDto jogoDto = new JogoDto();
		jogoDto.setCampeonato(campeonato);

		jogoDto.setNumeroRodada(numeroRodada);

		jogoDto.setEqpMandante(eqpMandante);
		jogoDto.setEqpMandantePrimeitoTempoTotalGol(eqpMandantePrimeitoTempoTotalGol);
		jogoDto.setEqpMandantePrimeitoTempoTotalEscanteio(eqpMandantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpMandanteSegundoTempoTotalGol(eqpMandantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpMandanteSegundoTempoTotalEscanteio(eqpMandanteSegundoTempoTotalEscanteio);


		jogoDto.setEqpVisitante(eqpVisitante);
		jogoDto.setEqpVisitantePrimeitoTempoTotalGol(eqpVisitantePrimeitoTempoTotalGol);
		jogoDto.setEqpVisitantePrimeitoTempoTotalEscanteio(eqpVisitantePrimeitoTempoTotalEscanteio);
		jogoDto.setEqpVisitanteSegundoTempoTotalGol(eqpVisitanteSegundoTempoTotalGol);
		jogoDto.setEqpVisitanteSegundoTempoTotalEscanteio(eqpVisitanteSegundoTempoTotalEscanteio);


		Response response = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(jogoDto)
                .when()
                .put("/jogo/v1/" + id);
		
		int statusCode = response.getStatusCode();
		
		return statusCode;		
	}
	




}