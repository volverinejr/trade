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
import br.com.claudemirojr.trade.model.repository.JogoRepository;
import br.com.claudemirojr.trade.testcontainer.AplicacaoStartTestContainer;
import br.com.claudemirojr.trade.util.AuthUtil;
import io.restassured.RestAssured;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JogoControllerTest extends AplicacaoStartTestContainer {
	@Autowired
	private JogoRepository jogoRepository;

	private String accessToken;
	private Long jogoId;
    static String campeonato;
    private String equipeMandante;
    private String equipeVisitante;
	
	
	@LocalServerPort
	private int port;	
	

	@BeforeAll
	void obterToken() {
		jogoRepository.deleteAll();

		accessToken = AuthUtil.obterToken();
		
		RestAssured.port = port;
		RestAssured.basePath = "/api/trade";
	} 
	
	@Test
	@Order(1)
	@Description("Criar um novo jogo")
	void create() {
		//criando o campeonato
		String nome = "Brasileirão série A 2023";
		String descricao = "Principal competição de pontos corridos do Brasil.";
		Boolean ativo = true;
		
		CampeonatoDto campeonatoDto = new CampeonatoDto();
		campeonatoDto.setNome(nome);
		campeonatoDto.setDescricao(descricao);
		campeonatoDto.setAtivo(ativo);
		
		var campeonatoResponseDto = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(campeonatoDto)
                .when()
                .post("/campeonato/v1")
                .then()
                .statusCode(201)
                .extract()
                .as(CampeonatoResponseDto.class);
		assertNotNull(campeonatoResponseDto);
		
		var campeonato = ModelMaperConverter.parseObject(campeonatoResponseDto, Campeonato.class);
		
		
		//criando equipe mandante
		String eqpMandante = "Cuiabá";
		
		EquipeDto equipeDto = new EquipeDto(eqpMandante);
		equipeDto.setNome(eqpMandante);
		
		var equipeMandante = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(equipeDto)
                .when()
                .post("/equipe/v1")
                .then()
                .statusCode(201)
                .extract()
                .as(EquipeResponseDto.class);	
		assertNotNull(equipeMandante);
		
		var eqMandante = ModelMaperConverter.parseObject(equipeMandante, Equipe.class);

		
		
		//criando equipe visitante
		equipeDto = new EquipeDto("Cruzeiro");
		var equipeVisitante = RestAssured.given()
        		.header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(equipeDto)
                .when()
                .post("/equipe/v1")
                .then()
                .statusCode(201)
                .extract()
                .as(EquipeResponseDto.class);		
		assertNotNull(equipeVisitante);
		
		var eqVisitante = ModelMaperConverter.parseObject(equipeVisitante, Equipe.class);		
		
		
		JogoDto jogoDto = new JogoDto();
		jogoDto.setCampeonato(campeonato);
		
		jogoDto.setNumeroRodada(26);
		jogoDto.setEqpMandante(eqMandante);
		jogoDto.setEqpVisitante(eqVisitante);

		jogoDto.setEqpMandantePrimeitoTempoTotalGol(0);
		jogoDto.setEqpMandantePrimeitoTempoTotalEscanteio(2);
		jogoDto.setEqpMandanteSegundoTempoTotalGol(0);
		jogoDto.setEqpMandanteSegundoTempoTotalEscanteio(3);

		jogoDto.setEqpVisitantePrimeitoTempoTotalGol(0);
		jogoDto.setEqpVisitantePrimeitoTempoTotalEscanteio(1);
		jogoDto.setEqpVisitanteSegundoTempoTotalGol(0);
		jogoDto.setEqpVisitanteSegundoTempoTotalEscanteio(4);

		
		jogoId = RestAssured.given()
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
		
		System.out.println("ID do Jogo gerado: " + jogoId);
		assertNotNull(jogoId);
	}
	
	@Test
	@Order(2)
	@Description("Buscar jogo criado por ID e validar nome")
	void findByAuditId() {
		String cameponatoEsperado = "Brasileirão série A 2023";
	
        
        JogoResponseDto jogoResponseDto = RestAssured.given()
        	    .header("Authorization", "Bearer " + accessToken)
        	    .contentType("application/json")
        	    .when()
        	    .get("/jogo/v1/" + jogoId)
        	    .then()
        	    .statusCode(200)
        	    .extract()
        	    .as(JogoResponseDto.class);
		
		assertEquals(cameponatoEsperado, jogoResponseDto.getCampeonato().getNome());
	}
	
	
	
	@Test
	@Order(3)
	@Description("Atualizar os dados do jogo")
	void update() {
		String nomeCamp = "Brasileirão série A 2023";
		String descricao = "Principal competição de pontos corridos do Brasil.";
		Boolean ativo = true;
		
		Integer numeroRodadaModificado = 28;

	    Campeonato campeonato = new Campeonato();
	    campeonato.setNome(nomeCamp);
	    campeonato.setDescricao(descricao);
	    campeonato.setAtivo(ativo);
	    
	    
	    String eqpMandante = "Cuiabá";
		
		EquipeDto equipeDto = new EquipeDto(eqpMandante);
		equipeDto.setNome(eqpMandante);

	    Equipe visitante = new Equipe();
	    visitante.setNome(equipeVisitante);

	    JogoDto jogoDto = new JogoDto();
	    jogoDto.setNumeroRodada(numeroRodadaModificado);
	    jogoDto.setCampeonato(campeonato);
	    jogoDto.setEqpMandante(eqMandante);
	    jogoDto.setEqpVisitante(visitante);

	    jogoDto.setEqpMandantePrimeitoTempoTotalGol(1);
	    jogoDto.setEqpMandantePrimeitoTempoTotalEscanteio(2);
	    jogoDto.setEqpMandanteSegundoTempoTotalGol(1);
	    jogoDto.setEqpMandanteSegundoTempoTotalEscanteio(3);

	    jogoDto.setEqpVisitantePrimeitoTempoTotalGol(0);
	    jogoDto.setEqpVisitantePrimeitoTempoTotalEscanteio(2);
	    jogoDto.setEqpVisitanteSegundoTempoTotalGol(1);
	    jogoDto.setEqpVisitanteSegundoTempoTotalEscanteio(4);

	    RestAssured.given()
	        .header("Authorization", "Bearer " + accessToken)
	        .contentType("application/json")
	        .body(jogoDto)
	        .log().all()
	        .when()
	        .put("/jogo/v1/" + jogoId)
	        .then()
	        .log().all()
	        .statusCode(200);

	    // Confirmação da atualização
	    JogoResponseDto jogoResponseDto = RestAssured.given()
	        .header("Authorization", "Bearer " + accessToken)
	        .contentType("application/json")
	        .when()
	        .get("/jogo/v1/" + jogoId)
	        .then()
	        .statusCode(200)
	        .extract()
	        .as(JogoResponseDto.class);

	    assertEquals(numeroRodadaModificado, jogoResponseDto.getNumeroRodada());
	}
	
	
}