package br.com.claudemirojr.trade.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
import br.com.claudemirojr.trade.model.repository.EquipeRepository;
import br.com.claudemirojr.trade.testcontainer.AplicacaoStartTestContainer;
import br.com.claudemirojr.trade.util.ApiRequestBuilder;
import br.com.claudemirojr.trade.util.AuthUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
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
	void setup() {
		equipeRepository.deleteAll();

		accessToken = AuthUtil.obterToken();

		RestAssured.port = port;
		RestAssured.basePath = "/api/trade";

		// Define uma especificação padrão
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON)
				.addHeader("Authorization", "Bearer " + accessToken).build();
	}

	@Test
	@Order(1)
	@Description("Criar uma nova equipe chamada Bahia")
	void deveCriarEquipeComSucesso() {
		EquipeDto equipeDto = new EquipeDto("Bahia");

		var response = new ApiRequestBuilder()
				.url("/equipe/v1")
				.method("POST")
				.body(equipeDto)
				.execute();

		response.then().statusCode(201)
		.body("nome", equalTo("Bahia"));

		equipeId = response.jsonPath()
				.getLong("id");
		assertNotNull(equipeId);

	}

	@Test
	@Order(2)
	@Description("Buscar equipe criada por ID e validar nome")
	void deveBuscarEquipePeloIdValidarNome() {
		var response = new ApiRequestBuilder()
				.url("/equipe/v1/" + equipeId)
				.execute();

		response.then().statusCode(200)
		.body("nome", equalTo("Bahia"));
		
	}

	@Test
	@Order(3)
	@Description("Atualizar o nome da equipe para Bahia Campeão")
	void deveAtualizarCampeonatoComSucesso() {
		String equipeModificada = "Bahia Campeão";
		EquipeDto equipeDto = new EquipeDto(equipeModificada);
		
        var response = new ApiRequestBuilder()
                .url("/equipe/v1/" + equipeId)
                .method("PUT")
                .body(equipeDto)
                .execute();
        
        
        response.then()
    	.statusCode(200)
    	.body("nome", equalTo(equipeModificada));
        
        
        
        response = new ApiRequestBuilder()
                .url("/equipe/v1/" + equipeId)
                .execute();			
		
		
        response.then()
    	.statusCode(200)
    	.body("nome", equalTo(equipeModificada));      		
			
		
	}
	


	@Test
	@Order(4)
	@Description("Atualizar equipe inexistente")
	void deveAtualizarEquipeInexistenteComError() {
		String equipeModificada = "Vitória Campeão - :-)";
		EquipeDto equipeDto = new EquipeDto(equipeModificada);
		
		  var response = new ApiRequestBuilder()
	                .url("/equipe/v1/" + 10)
	                .method("PUT")
	                .body(equipeDto)
	                .execute();

	        response.then()
	    	.statusCode(404);
	}

	@Test
	@Order(5)
	@Description("Listar todas as equipes - com paginação")
	void deveListarEquipe() {
		EquipeDto equipeDto = new EquipeDto("Man City");
		
		
		 var response = new ApiRequestBuilder()
	                .url("/equipe/v1")
	                .method("POST")
	                .body(equipeDto)
	                .execute();	
	        
	        response.then()
	        	.statusCode(201);
	        
	        List<EquipeResponseDto> equipeResponseDto = new ApiRequestBuilder()
	                .url("/equipe/v1")
	                .executeAndExtractList("content", EquipeResponseDto.class, 200);
	        
			assertEquals(2, equipeResponseDto.size());

	}

	@Test
	@Order(6)
	@Description("Listar todas as equipes cujo ID >= 2")
	void deveListarEquipeComIdMaiorIgual() {
		Long ultimoCadastro = equipeId + 1;
		
		

        List<EquipeResponseDto> equipeResponseDto = new ApiRequestBuilder()
                .url("/equipe/v1/search-id-maior-igual/" + ultimoCadastro)
                .executeAndExtractList("content", EquipeResponseDto.class, 200);
		
		assertEquals(1, equipeResponseDto.size());
	}

	@Test
	@Order(7)
	@Description("Listar a equipe pelo nome")
	void deveListarEquipeNomeContem() {
		String equipeModificada = "Bahia Campeão";
		
		
		List<EquipeResponseDto> equipeResponseDto = new ApiRequestBuilder()
                .url("/equipe/v1/search-nome/" + equipeModificada)
                .executeAndExtractList("content", EquipeResponseDto.class, 200);
		
		assertEquals(1, equipeResponseDto.size());
		
	}

	@Test
	@Order(8)
	@Description("Listar todas as equipe - sem paginação")
	void deveListarEquipeAtivo() {
		
		  List<EquipeResponseDto> equipeResponseDto = new ApiRequestBuilder()
	                .url("/equipe/v1/search-all")
	                .executeAndExtractList("content", EquipeResponseDto.class, 200);
			
		  assertEquals(2, equipeResponseDto.size());
	}

	@Test
	@Order(9)
	@Description("Filtar as equipes por ID ou Nome")
	void deveListarEquipelPorIdOrNome() {		
		
		List<EquipeResponseDto> equipeResponseDto = new ApiRequestBuilder()
                .url("/equipe/v1/filter/" + 1)
                .executeAndExtractList("content", EquipeResponseDto.class, 200);
		assertEquals(2, equipeResponseDto.size());
		
		
		
		
        List<EquipeResponseDto> EquipeResponseDto = new ApiRequestBuilder()
                .url("/equipe/v1/filter/" + "Bahia")
                .executeAndExtractList("content", EquipeResponseDto.class, 200);
		assertEquals(1, EquipeResponseDto.size());
		
	}

	@Test
	@Order(10)
	@Description("Excluir a Equipe id=1")
	void deveExcluirEquipeComSucesso() {
        var response = new ApiRequestBuilder()
                .url("/equipe/v1/" + equipeId)
                .method("DELETE")
                .execute();	
        
        response.then()
        	.statusCode(200);
		
	}

	@Test
	@Order(11)
	@Description("Excluir a Equipe id=10 que não existe")
	void deveExcluirEquipeInexistenteComError() {		
		var response = new ApiRequestBuilder()
                .url("/equipe/v1/" + equipeId)
                .method("DELETE")
                .execute();	
        
        response.then()
        	.statusCode(404);
	}

	

}
