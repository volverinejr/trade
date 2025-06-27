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
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;
import br.com.claudemirojr.trade.testcontainer.AplicacaoStartTestContainer;
import br.com.claudemirojr.trade.util.AuthUtil;
import io.restassured.RestAssured;

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
		String nome = "Premier League 2023/2023";
		String descricao = "Principal competição de pontos corridos da Inglaterra";
		Boolean ativo = true;

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
		
		assertNotNull(campeonatoId);
	}
	
	@Test
	@Order(2)
	@Description("Buscar campeonato criado por ID e validar nome")
	void findByAuditId() {
		String campeonatoEsperado = "Premier League 2023/2023";

		CampeonatoResponseDto campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/" + campeonatoId)
                .then()
                .statusCode(200)
                .extract()
                .as(CampeonatoResponseDto.class);
		
		assertEquals(campeonatoEsperado, campeonatoResponseDto.getNome());
	}
	
	
	@Test
	@Order(3)
	@Description("Atualizar o nome do campeonato para Brasileirão")
	void update() {
		String campeonatoModificado = "Brasileirão";
		String descricao = "Principal competição de pontos corridos do Brasil";
		Boolean ativo = true;
		
		CampeonatoDto campeonatoDto = new CampeonatoDto();
		campeonatoDto.setNome(campeonatoModificado);
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
		
		
		//Confirmar que foi alterado
		CampeonatoResponseDto campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/" + campeonatoId)
                .then()
                .statusCode(200)
                .extract()
                .as(CampeonatoResponseDto.class);
		
		assertEquals(campeonatoModificado, campeonatoResponseDto.getNome());
	}
	
	@Test
	@Order(4)
	@Description("Atualizar o nome do campeonato para Brasileirão")
	void updateregistroInexistente() {
		String campeonatoModificado = "Brasileirão";
		String descricao = "Principal competição de pontos corridos do Brasil";
		Boolean ativo = true;
		
		CampeonatoDto campeonatoDto = new CampeonatoDto();
		campeonatoDto.setNome(campeonatoModificado);
		campeonatoDto.setDescricao(descricao);
		campeonatoDto.setAtivo(ativo);

		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(campeonatoDto)
                .when()
                .put("/campeonato/v1/10")
                .then()
                .statusCode(404);
	}	
	
	
	@Test
	@Order(5)
	@Description("Listar todos os campeonatos")
	void findAll() {
		String campeonatoModificado = "Série B";
		String descricao = "Principal competição de pontos corridos do Brasil";
		Boolean ativo = true;

		CampeonatoDto campeonatoDto = new CampeonatoDto();
		campeonatoDto.setNome(campeonatoModificado);
		campeonatoDto.setDescricao(descricao);
		campeonatoDto.setAtivo(ativo);

		//inserindo mais um registro no banco
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(campeonatoDto)
                .when()
                .post("/campeonato/v1")
                .then()
                .statusCode(201);

		//listando todas as equipes
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
		
		assertEquals(2, campeonatoResponseDto.size());
	}
	
	
	@Test
	@Order(6)
	@Description("Listar todos os campeonatos cujo ID >= 2")
	void findAllIdMaiorIgual() {
		//listando todos os campeonatos
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/search-id-maior-igual/" + 2)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);   
		
		assertEquals(1, campeonatoResponseDto.size());
	}
	
	
	@Test
	@Order(7)
	@Description("Listar o campeonato pelo nome")
	void findAllNomeContem() {
		String campeonatoModificado = "Série B";
		
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/search-nome/" + campeonatoModificado)
                .then()
                .statusCode(200)
                .extract()
                //.as(new TypeRef<List<EquipeResponseDto>>() {});
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);   
		
		assertEquals(1, campeonatoResponseDto.size());
	}
	
	
	@Test
	@Order(8)
	@Description("Listar todos os campeonatos ativos")
	void carregarTodosOsCampeonatosAtivos() {
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
		
		assertEquals(2, campeonatoResponseDto.size());
	}
	
	
	@Test
	@Order(9)
	@Description("Filtar todos os campeonatos por ID ou Nome")
	void filterAllPorIdOrNome() {
		List<CampeonatoResponseDto> campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/filter/" + 1)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);   
		
		assertEquals(2, campeonatoResponseDto.size());
		
		
		
		campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/filter/Série B")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", CampeonatoResponseDto.class);   
		
		assertEquals(1, campeonatoResponseDto.size());
		
	}
	
	@Test
	@Order(10)
	@Description("Excluir o Campeonato id=1")
	void delete() {
		//Excluir o registro id=1
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("/campeonato/v1/" + campeonatoId)
                .then()
                .statusCode(200);
		
		//Confirmar se o registro foi excluido
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/campeonato/v1/" + campeonatoId)
                .then()
                .statusCode(404);
		
	}
	
	
	@Test
	@Order(11)
	@Description("Excluir campeonato id=10 que não existe")
	void deleteRegistroInexistente() {
		//Excluir o registro id=10
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("/campeonato/v1/10")
                .then()
                .statusCode(404);
	}
		
	

}
