package br.com.claudemirojr.trade.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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

import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;
import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;
import br.com.claudemirojr.trade.testcontainer.AplicacaoStartTestContainer;
import br.com.claudemirojr.trade.util.AuthUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CampeonatoControllerTest extends AplicacaoStartTestContainer {
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;

	private String accessToken;
	private Long campeonatoId;
	
	@LocalServerPort
	private int port;	
	

	@BeforeAll
	void obterToken() {
		campeonatoRepository.deleteAll();

		accessToken = AuthUtil.obterToken();
		
		RestAssured.port = port;
		RestAssured.basePath = "/api/trade";
	}
	
	
	@Test
	@Order(1)
	@Description("Criar um novo campeonato chamado Premier League 2023/2023")
	void create() {
		campeonatoId = criarCampeonato("Premier League 2023/2023","Principal competição de pontos corridos da Inglaterra",true);

		assertNotNull(campeonatoId);
	}
	
	@Test
	@Order(2)
	@Description("Buscar campeonato criado por ID e validar nome")
	void findByAuditId() {
		String campeonatoEsperado = "Premier League 2023/2023";

		CampeonatoResponseDto campeonatoResponseDto = buscarCampeonato(campeonatoId);

		assertEquals(campeonatoEsperado, campeonatoResponseDto.getNome());
	}
	
	
	@Test
	@Order(3)
	@Description("Atualizar o nome do campeonato para Brasileirão")
	void update() {
		String campeonatoModificado = "Brasileirão";
		atualizarCampeonato(campeonatoId, campeonatoModificado , "Principal competição de pontos corridos do Brasil" , true);

		//Confirmar que foi alterado
		CampeonatoResponseDto campeonatoResponseDto = buscarCampeonato(campeonatoId);

		assertEquals(campeonatoModificado, campeonatoResponseDto.getNome());
	}

	
	@Test
	@Order(4)
	@Description("Atualizar campeonato inexistente")
	void updateEquipeInexistente() {
		String campeonatoModificado = "La liga";
		
		int statusCode = atualizarCampeonatoInexistente(100L, campeonatoModificado,"Campeonato espanhol",true);
		
		assertEquals(404, statusCode);
	}

	
	
	
	@Test
	@Order(5)
	@Description("Listar todos os campeonatos - com paginação")
	void findAll() {
		String nome = "Série B";
		String descricao = "Principal competição de pontos corridos do Brasil";
		Boolean ativo = true;
		criarCampeonato(nome,descricao,ativo);

		List<CampeonatoResponseDto> campeonatoResponseDto = listarTodosOsCampeonatos();

		assertEquals(2, campeonatoResponseDto.size());
	}

	
	
	@Test
	@Order(6)
	@Description("Listar todos os campeonatos cujo ID >= 2")
	void findAllIdMaiorIgual() {
		List<CampeonatoResponseDto> campeonatoResponseDto = listarTodosOsCampeonatosIdMaiorOuIgual(2L);

		assertEquals(1, campeonatoResponseDto.size());
	}

	
	
	
	@Test
	@Order(7)
	@Description("Listar o campeonato pelo nome")
	void findAllNomeContem() {
		String campeonatoModificado = "Série B";

		List<CampeonatoResponseDto> campeonatoResponseDto = listarTodosOsCampeonatosContemNome(campeonatoModificado);

		assertEquals(1, campeonatoResponseDto.size());
	}
	
	
	
	@Test
	@Order(8)
	@Description("Listar todos os campeonatos ativos")
	void carregarTodasAsEquipes() {
		List<CampeonatoResponseDto> campeonatoResponseDto = listarTodosOsCampeonatosAtivos();

		assertEquals(2, campeonatoResponseDto.size());
	}
	
	
	@Test
	@Order(9)
	@Description("Filtar todos os campeonatos por ID ou Nome")
	void filterAllPorIdOrNome() {
		List<CampeonatoResponseDto> campeonatoResponseDto = filtroPorIdMaiorOuIgual(1L);
		assertEquals(2, campeonatoResponseDto.size());

		campeonatoResponseDto = filtroPorNome("Série B");
		assertEquals(1, campeonatoResponseDto.size());
	}

	
	
	@Test
	@Order(10)
	@Description("Excluir o Campeonato id=1")
	void delete() {
		excluirCampeonato(campeonatoId);

		//Confirmar se o registro foi excluido
		int statusCode = campeonatoInexistente(campeonatoId);
		
		assertEquals(404, statusCode);
	}

	@Test
	@Order(11)
	@Description("Excluir campeonato id=10 que não existe")
	void deleteRegistroInexistente() {
		int statusCode = campeonatoInexistente(10L);
		
		assertEquals(404, statusCode);
	}
	
	

	
	
	private Long criarCampeonato(String nome,String descricao,Boolean ativo) {
		CampeonatoDto campeonatoDto = new CampeonatoDto();
		campeonatoDto.setNome(nome);
		campeonatoDto.setDescricao(descricao);
		campeonatoDto.setAtivo(ativo);
		
		campeonatoId = RestAssured.given()
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
	
	
	private void atualizarCampeonato(Long id,String nome,String descricao,Boolean ativo) {
		CampeonatoDto campeonatoDto = new CampeonatoDto();
		campeonatoDto.setNome(nome);
		campeonatoDto.setDescricao(descricao);
		campeonatoDto.setAtivo(ativo);


		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(campeonatoDto)
                .when()
                .put("/campeonato/v1/" + campeonatoId)
                .then()
                .statusCode(200);
	}
	
	
	private int atualizarCampeonatoInexistente(Long id,String nome,String descricao,Boolean ativo) {
		CampeonatoDto campeonatoDto = new CampeonatoDto();
		campeonatoDto.setNome(nome);
		campeonatoDto.setDescricao(descricao);
		campeonatoDto.setAtivo(ativo);

		Response response = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(campeonatoDto)
                .when()
                .put("/campeonato/v1/" + id);
		
		int statusCode = response.getStatusCode();
		
		return statusCode;
	}
	
	
	
	
	private List<CampeonatoResponseDto> listarTodosOsCampeonatos() {
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);

		return campeonatoResponseDto;
	}
	
	
	private List<CampeonatoResponseDto> listarTodosOsCampeonatosAtivos() {
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/search-ativo")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);

		return campeonatoResponseDto;
	}
	
	
	


	
	private List<CampeonatoResponseDto> listarTodosOsCampeonatosIdMaiorOuIgual(Long id) {
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/search-id-maior-igual/" + id)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);

		return campeonatoResponseDto;
	}
	
	
	
	
	private List<CampeonatoResponseDto> listarTodosOsCampeonatosContemNome(String nome) {
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/search-nome/" + nome)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);

		return campeonatoResponseDto;

	}
	
	
	private List<CampeonatoResponseDto> filtroPorIdMaiorOuIgual(Long id) {
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/filter/" + id)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);

		return campeonatoResponseDto;
	}
	
	
	
	private List<CampeonatoResponseDto> filtroPorNome(String nome) {
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/filter/" + nome)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);

		return campeonatoResponseDto;
	}
	
	private void excluirCampeonato(Long id) {
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("/campeonato/v1/" + id)
                .then()
                .statusCode(200);
	}
	

	
	
	
	private int campeonatoInexistente(Long id) {
		Response response =  RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/" + id);
		
		int statusCode = response.getStatusCode();
		
		return statusCode;
	}
	
	
	
	


	



}
