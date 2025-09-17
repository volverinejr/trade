package br.com.claudemirojr.trade.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;
import br.com.claudemirojr.trade.testcontainer.AplicacaoStartTestContainer;
import br.com.claudemirojr.trade.util.ApiRequestBuilder;
import br.com.claudemirojr.trade.util.AuthUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CampeonatoControllerTest extends AplicacaoStartTestContainer {
	//testando feature miro0-1

	@Autowired
	private CampeonatoRepository campeonatoRepository;

	private String accessToken;
	private Long campeonatoId;

	@LocalServerPort
	private int port;

	@BeforeAll
	void setup() {
		campeonatoRepository.deleteAll();

		accessToken = AuthUtil.obterToken();

		RestAssured.port = port;
		RestAssured.basePath = "/api/trade";
		
		// Define uma especificação padrão
		RestAssured.requestSpecification = new RequestSpecBuilder()
	            .setContentType(ContentType.JSON)
	            .addHeader("Authorization", "Bearer " + accessToken)
	            .build();		
	}
	

	@Test
	@Order(1)
	@DisplayName("Criar um novo campeonato chamado [Premier League 2023/2023]")
	void deveCriarCampeonatoComSucesso() {
		CampeonatoDto campeonatoDto = new CampeonatoDto(
				"Premier League 2023/2023",
				"Principal competição de pontos corridos da Inglaterra", 
				true);
		
        var response = new ApiRequestBuilder()
                .url("/campeonato/v1")
                .method("POST")
                .body(campeonatoDto)
                .execute();	
        
        response.then()
        	.statusCode(201)
        	.body("nome", equalTo("Premier League 2023/2023"));      
        
        campeonatoId = response.jsonPath().getLong("id");
		assertNotNull(campeonatoId);
		
		
		/*
		CampeonatoResponseDto dto = response.then()
		        .statusCode(201)
		        .extract()
		        .as(CampeonatoResponseDto.class);

		    assertEquals("Brasileirão 2025", dto.getNome());
		    assertEquals(2025, dto.getEdicao());
		    assertNotNull(dto.getId()); // já valida que veio um id
		*/
	}

	@Test
	@Order(2)
	@DisplayName("Buscar campeonato criado por [ID e validar nome]")
	void deveBuscarCampeonatoPeloIdValidarNome() {
        var response = new ApiRequestBuilder()
                .url("/campeonato/v1/" + campeonatoId)
                .execute();			
		
		
        response.then()
    	.statusCode(200)
    	.body("nome", equalTo("Premier League 2023/2023"));      		
	}

	@Test
	@Order(3)
	@DisplayName("Atualizar o nome do campeonato para [Brasileirão]")
	void deveAtualizarCampeonatoComSucesso() {
		String campeonatoModificado = "Brasileirão";
		CampeonatoDto campeonatoDto = new CampeonatoDto(campeonatoModificado, "Principal competição de pontos corridos do Brasil", true);
		
        var response = new ApiRequestBuilder()
                .url("/campeonato/v1/" + campeonatoId)
                .method("PUT")
                .body(campeonatoDto)
                .execute();
        
        
        response.then()
    	.statusCode(200)
    	.body("nome", equalTo(campeonatoModificado));
        
        
        
        response = new ApiRequestBuilder()
                .url("/campeonato/v1/" + campeonatoId)
                .execute();			
		
		
        response.then()
    	.statusCode(200)
    	.body("nome", equalTo(campeonatoModificado));      		
	}

	@Test
	@Order(4)
	@DisplayName("Atualizar [campeonato Inexistente id=10]")
	void deveAtualizarCampeonatoInexistenteComError() {
		CampeonatoDto campeonatoDto = new CampeonatoDto("La liga", "Campeonato espanhol", true);
		
        var response = new ApiRequestBuilder()
                .url("/campeonato/v1/" + 10)
                .method("PUT")
                .body(campeonatoDto)
                .execute();

        response.then()
    	.statusCode(404);
	}

	@Test
	@Order(5)
	@DisplayName("Listar todos os campeonatos - com paginação")
	void deveListarCampeonato() {
		CampeonatoDto campeonatoDto = new CampeonatoDto(
				"Série B",
				"Principal competição de pontos corridos do Brasil", 
				true);
		
        var response = new ApiRequestBuilder()
                .url("/campeonato/v1")
                .method("POST")
                .body(campeonatoDto)
                .execute();	
        
        response.then()
        	.statusCode(201);
        
        
        List<CampeonatoResponseDto> campeonatoResponseDto = new ApiRequestBuilder()
                .url("/campeonato/v1")
                .executeAndExtractList("content", CampeonatoResponseDto.class, 200);
        
		assertEquals(2, campeonatoResponseDto.size());
	}

	@Test
	@Order(6)
	@DisplayName("Listar todos os campeonatos cujo ID >= 2")
	void deveListarCampeonatoComIdMaiorIgual() {
		Long ultimoCadastro = campeonatoId + 1;
		
        List<CampeonatoResponseDto> campeonatoResponseDto = new ApiRequestBuilder()
                .url("/campeonato/v1/search-id-maior-igual/" + ultimoCadastro)
                .executeAndExtractList("content", CampeonatoResponseDto.class, 200);
		
		assertEquals(1, campeonatoResponseDto.size());
	}

	@Test
	@Order(7)
	@DisplayName("Listar o [campeonato pelo nome]")
	void deveListarCampeonatoNomeContem() {
		String campeonatoModificado = "Série B";

        List<CampeonatoResponseDto> campeonatoResponseDto = new ApiRequestBuilder()
                .url("/campeonato/v1/search-nome/" + campeonatoModificado)
                .executeAndExtractList("content", CampeonatoResponseDto.class, 200);
		
		assertEquals(1, campeonatoResponseDto.size());
	}

	@Test
	@Order(8)
	@DisplayName("Listar todos os [campeonatos ativos]")
	void deveListarCampeonatoAtivo() {
        List<CampeonatoResponseDto> campeonatoResponseDto = new ApiRequestBuilder()
                .url("/campeonato/v1/search-ativo")
                .executeAndExtractList("content", CampeonatoResponseDto.class, 200);
		
		assertEquals(2, campeonatoResponseDto.size());
	}

	@Test
	@Order(9)
	@DisplayName("Filtar todos os campeonatos por ID ou Nome")
	void deveListarCampeonatolPorIdOrNome() {
        List<CampeonatoResponseDto> campeonatoResponseDtoFilter = new ApiRequestBuilder()
                .url("/campeonato/v1/filter/" + 1)
                .executeAndExtractList("content", CampeonatoResponseDto.class, 200);
		assertEquals(2, campeonatoResponseDtoFilter.size());
		
		
		
		
        List<CampeonatoResponseDto> campeonatoResponseDtoNome = new ApiRequestBuilder()
                .url("/campeonato/v1/filter/" + "Série B")
                .executeAndExtractList("content", CampeonatoResponseDto.class, 200);
		assertEquals(1, campeonatoResponseDtoNome.size());
	}

	@Test
	@Order(10)
	@DisplayName("Excluir o Campeonato [id=1]")
	void deveExcluirCampeonatoComSucesso() {
        var response = new ApiRequestBuilder()
                .url("/campeonato/v1/" + campeonatoId)
                .method("DELETE")
                .execute();	
        
        response.then()
        	.statusCode(200);
	}

	@Test
	@Order(11)
	@DisplayName("Excluir campeonato inexistente [id=1]")
	void deveExcluirCampeonatoInexistenteComError() {
        var response = new ApiRequestBuilder()
                .url("/campeonato/v1/" + campeonatoId)
                .method("DELETE")
                .execute();	
        
        response.then()
        	.statusCode(404);
	}
	
}
