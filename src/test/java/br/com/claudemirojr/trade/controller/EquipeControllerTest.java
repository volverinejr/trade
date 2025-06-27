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
		String nome = "Bahia";

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
	
		
		System.out.println("ID da Equipe gerada: " + equipeId);
		assertNotNull(equipeId);
		
	}
	
	
	@Test
	@Order(2)
	@Description("Buscar equipe criada por ID e validar nome")
	void findByAuditId() {
		String equipeEsperada = "Bahia";

		EquipeResponseDto equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/" + equipeId)
                .then()
                .statusCode(200)
                .extract()
                .as(EquipeResponseDto.class);
		
		assertEquals(equipeEsperada, equipeResponseDto.getNome());
	}
	
	
	@Test
	@Order(3)
	@Description("Atualizar o nome da equipe para Bahia Campeão")
	void update() {
		String equipeModificada = "Bahia Campeão";
		
		EquipeDto equipeDto = new EquipeDto();
		equipeDto.setNome(equipeModificada);

		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(equipeDto)
                .when()
                .put("/equipe/v1/" + equipeId)
                .then()
                .statusCode(200);
		
		
		//Confirmar que foi alterado
		EquipeResponseDto equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/" + equipeId)
                .then()
                .statusCode(200)
                .extract()
                .as(EquipeResponseDto.class);
		
		assertEquals(equipeModificada, equipeResponseDto.getNome());
	}
	
	
	
	
	@Test
	@Order(4)
	@Description("Atualizar o nome da equipe para Bahia Campeão")
	void updateregistroInexistente() {
		String equipeModificada = "Bahia Campeão";
		
		EquipeDto equipeDto = new EquipeDto();
		equipeDto.setNome(equipeModificada);

		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(equipeDto)
                .when()
                .put("/equipe/v1/10")
                .then()
                .statusCode(404);
	}	
	
	
	
	
	@Test
	@Order(5)
	@Description("Listar todas as equipes")
	void findAll() {
		String nome = "Man City";

		EquipeDto equipeDto = new EquipeDto();
		equipeDto.setNome(nome);

		//inserindo mais um registro no banco
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(equipeDto)
                .when()
                .post("/equipe/v1")
                .then()
                .statusCode(201);

		//listando todas as equipes
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
		
		assertEquals(2, equipeResponseDto.size());
	}
	
	
	
	
	
	@Test
	@Order(6)
	@Description("Listar todas as equipes cujo ID >= 2")
	void findAllIdMaiorIgual() {
		//listando todas as equipes
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/search-id-maior-igual/" + 2)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);   
		
		assertEquals(1, equipeResponseDto.size());
	}		
	
	
	@Test
	@Order(7)
	@Description("Listar a equipe pelo nome")
	void findAllNomeContem() {
		String equipeModificada = "Bahia Campeão";
		
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/search-nome/" + equipeModificada)
                .then()
                .statusCode(200)
                .extract()
                //.as(new TypeRef<List<EquipeResponseDto>>() {});
                .jsonPath()
                .getList("content", EquipeResponseDto.class);   
		
		assertEquals(1, equipeResponseDto.size());
	}
	

	
	
	
	@Test
	@Order(8)
	@Description("Listar todas as equipe")
	void carregarTodasAsEquipes() {
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
		
		assertEquals(2, equipeResponseDto.size());
	}
	
	
	
	
		
	
	@Test
	@Order(9)
	@Description("Filtar as equipes por ID ou Nome")
	void filterAllPorIdOrNome() {
		List<EquipeResponseDto> equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/filter/" + 1)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);   
		
		assertEquals(2, equipeResponseDto.size());
		
		
		
		equipeResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/filter/Bahia")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", EquipeResponseDto.class);   
		
		assertEquals(1, equipeResponseDto.size());
		
	}
		
	
	@Test
	@Order(10)
	@Description("Excluir a Equipe id=1")
	void delete() {
		//Excluir o registro id=1
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("/equipe/v1/" + equipeId)
                .then()
                .statusCode(200);
		
		//Confirmar se o registro foi excluido
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/equipe/v1/" + equipeId)
                .then()
                .statusCode(404);
		
	}
	
	
	@Test
	@Order(11)
	@Description("Excluir a Equipe id=10 que não existe")
	void deleteRegistroInexistente() {
		//Excluir o registro id=10
		RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("/equipe/v1/10")
                .then()
                .statusCode(404);
	}		
	
	
	
	
	
}
