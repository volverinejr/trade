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

import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;
import br.com.claudemirojr.trade.model.repository.EquipeRepository;
import br.com.claudemirojr.trade.testcontainer.AplicacaoStartTestContainer;
import br.com.claudemirojr.trade.util.AuthUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquipeControllerTest extends AplicacaoStartTestContainer {

	@Autowired
	private EquipeRepository equipeRepository;

	private String accessToken;
	private Long equipeId;


	@LocalServerPort
	private int port;


	@BeforeAll
	void obterToken() {
		equipeRepository.deleteAll();

		accessToken = AuthUtil.obterToken();

		RestAssured.port = port;
		RestAssured.basePath = "/api/trade";
	}


	@Test
	@Order(1)
	@Description("Criar uma nova equipe chamada Bahia")
	void create() {
		equipeId = criarEquipe("Bahia");

		assertNotNull(equipeId);
	}


	@Test
	@Order(2)
	@Description("Buscar equipe criada por ID e validar nome")
	void findByAuditId() {
		String equipeEsperada = "Bahia";

		EquipeResponseDto equipeResponseDto = buscarEquipe(equipeId);

		assertEquals(equipeEsperada, equipeResponseDto.getNome());
	}


	@Test
	@Order(3)
	@Description("Atualizar o nome da equipe para Bahia Campeão")
	void update() {
		String equipeModificada = "Bahia Campeão";
		atualizarEquipe(equipeId, equipeModificada);

		//Confirmar que foi alterado
		EquipeResponseDto equipeResponseDto = buscarEquipe(equipeId);

		assertEquals(equipeModificada, equipeResponseDto.getNome());
	}


	@Test
	@Order(4)
	@Description("Atualizar equipe inexistente")
	void updateregistroInexistente() {
		String equipeModificada = "Vitória Campeão - :-)";
		
		int statusCode = atualizarEquipeInexistente(10L, equipeModificada);
		
		assertEquals(404, statusCode);
	}


	@Test
	@Order(5)
	@Description("Listar todas as equipes - com paginação")
	void findAll() {
		String nome = "Man City";
		criarEquipe(nome);

		List<EquipeResponseDto> equipeResponseDto = listarTodasAsEquipes();

		assertEquals(2, equipeResponseDto.size());
	}


	@Test
	@Order(6)
	@Description("Listar todas as equipes cujo ID >= 2")
	void findAllIdMaiorIgual() {
		List<EquipeResponseDto> equipeResponseDto = listarTodasAsEquipesIdMaiorOuIgual(2L);

		assertEquals(1, equipeResponseDto.size());
	}


	@Test
	@Order(7)
	@Description("Listar a equipe pelo nome")
	void findAllNomeContem() {
		String equipeModificada = "Bahia Campeão";

		List<EquipeResponseDto> equipeResponseDto = listarTodasAsEquipesContemNome(equipeModificada);

		assertEquals(1, equipeResponseDto.size());
	}


	@Test
	@Order(8)
	@Description("Listar todas as equipe - sem paginação")
	void carregarTodasAsEquipes() {
		List<EquipeResponseDto> equipeResponseDto = listarTodasAsEquipesSemPaginacao();

		assertEquals(2, equipeResponseDto.size());
	}


	@Test
	@Order(9)
	@Description("Filtar as equipes por ID ou Nome")
	void filterAllPorIdOrNome() {
		List<EquipeResponseDto> equipeResponseDto = filtroPorIdMaiorOuIgual(1L);
		assertEquals(2, equipeResponseDto.size());

		equipeResponseDto = filtroPorNome("Bahia");
		assertEquals(1, equipeResponseDto.size());
	}


	@Test
	@Order(10)
	@Description("Excluir a Equipe id=1")
	void delete() {
		excluirEquipe(equipeId);

		//Confirmar se o registro foi excluido
		int statusCode = equipeInexistente(equipeId);
		
		assertEquals(404, statusCode);
	}


	@Test
	@Order(11)
	@Description("Excluir a Equipe id=10 que não existe")
	void deleteRegistroInexistente() {
		int statusCode = equipeInexistente(10L);
		
		assertEquals(404, statusCode);
	}




	private Long criarEquipe(String nome) {
		EquipeDto equipeDto = new EquipeDto();
		equipeDto.setNome(nome);

		equipeId = RestAssured.given()
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


	private void atualizarEquipe(Long id, String nome) {
		EquipeDto equipeDto = new EquipeDto();
		equipeDto.setNome(nome);

		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(equipeDto)
                .when()
                .put("/equipe/v1/" + equipeId)
                .then()
                .statusCode(200);
	}
	
	
	private int atualizarEquipeInexistente(Long id, String nome) {
		EquipeDto equipeDto = new EquipeDto();
		equipeDto.setNome(nome);

		Response response = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(equipeDto)
                .when()
                .put("/equipe/v1/" + id);
		
		int statusCode = response.getStatusCode();
		
		return statusCode;
	}	
	

	private List<EquipeResponseDto> listarTodasAsEquipes() {
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);

		return equipeResponseDto;
	}


	private List<EquipeResponseDto> listarTodasAsEquipesSemPaginacao() {
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/search-all")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);

		return equipeResponseDto;
	}


	private List<EquipeResponseDto> listarTodasAsEquipesIdMaiorOuIgual(Long id) {
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/search-id-maior-igual/" + id)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);

		return equipeResponseDto;
	}


	private List<EquipeResponseDto> listarTodasAsEquipesContemNome(String nome) {
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/search-nome/" + nome)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);

		return equipeResponseDto;

	}


	private List<EquipeResponseDto> filtroPorIdMaiorOuIgual(Long id) {
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/filter/" + id)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);

		return equipeResponseDto;
	}


	private List<EquipeResponseDto> filtroPorNome(String nome) {
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/filter/" + nome)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);

		return equipeResponseDto;
	}


	private void excluirEquipe(Long id) {
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("/equipe/v1/" + id)
                .then()
                .statusCode(200);
	}
	
	private int equipeInexistente(Long id) {
		Response response =  RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/" + id);
		
		int statusCode = response.getStatusCode();
		
		return statusCode;
	}


}
