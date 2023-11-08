package br.com.claudemirojr.trade.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.claudemirojr.trade.dto.JogoAnaliseResponseDto;
import br.com.claudemirojr.trade.dto.JogoDadosResponseDto;
import br.com.claudemirojr.trade.dto.JogoDto;
import br.com.claudemirojr.trade.dto.JogoResponseDto;
import br.com.claudemirojr.trade.model.ParamsRequestModel;
import br.com.claudemirojr.trade.model.service.JogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Jogo", description = "Endpoints para gerenciamento dos jogos")
@RestController
@RequestMapping("${url.base}/jogo/v1")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TRADE_JOGO_ALL')")
public class JogoController {

	@Autowired
	private JogoService jogoService;

	@Operation(summary = "Insere um novo jogo")
	@PostMapping
	public ResponseEntity<JogoResponseDto> create(@RequestBody @Valid JogoDto jogoDto) {
		JogoResponseDto novo = jogoService.criar(jogoDto);

		return new ResponseEntity<>(novo, HttpStatus.CREATED);
	}

	@Operation(summary = "Atualiza os dados de um jogo")
	@PutMapping("/{id}")
	public ResponseEntity<JogoResponseDto> update(@PathVariable("id") Long id,
			@RequestBody @Valid JogoDto jogoDto) {
		JogoResponseDto existe = jogoService.atualizar(id, jogoDto);

		return new ResponseEntity<>(existe, HttpStatus.OK);
	}

	@Operation(summary = "Exclui um jogo")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		jogoService.delete(id);

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Busca os dados de todos os jogos")
	@GetMapping
	public ResponseEntity<?> findAll(@RequestParam Map<String, String> params) {
		ParamsRequestModel prm = new ParamsRequestModel(params);

		Page<JogoResponseDto> registros = jogoService.findAll(prm);

		return ResponseEntity.ok(registros);
	}

	@Operation(summary = "Filtra os dados dos jogos pelo id")
	@GetMapping("/search-id-maior-igual/{id}")
	public ResponseEntity<?> findAllIdMaiorIgual(@PathVariable Long id, @RequestParam Map<String, String> params) {
		ParamsRequestModel prm = new ParamsRequestModel(params);

		Page<JogoResponseDto> registros = jogoService.findAllIdMaiorIgual(id, prm);

		return ResponseEntity.ok(registros);
	}

	@Operation(summary = "Busca os dados de um jogo pelo id")
	@GetMapping("/{id}")
	public JogoResponseDto findByAuditId(@PathVariable("id") Long id) {
		return jogoService.findById(id);
	}
	
	
	@Operation(summary = "Análise do mandante X visitante em um determinado campeonato, especificando o número de jogos a ser analisado")
	@GetMapping("/analise/campeonato/{idCampeonato}/mandante/{idMandante}/visitante/{idVisitante}/jogos/{limiteDeJogos}")
	public JogoAnaliseResponseDto findByAnaliseMandanteXVisitante( @PathVariable Long idCampeonato, @PathVariable Long idMandante, @PathVariable Long idVisitante, @PathVariable Long limiteDeJogos) {
		return jogoService.findByAnaliseMandanteXVisitante(idCampeonato, idMandante, idVisitante, limiteDeJogos);
	}
	
	
	@Operation(summary = "Jogos do mandante X visitante em um determinado campeonato, especificando o número de jogos a ser analisado")
	@GetMapping("/dados/campeonato/{idCampeonato}/mandante/{idMandante}/visitante/{idVisitante}/jogos/{limiteDeJogos}")
	public JogoDadosResponseDto findByJogoMandanteXVisitante( @PathVariable Long idCampeonato, @PathVariable Long idMandante, @PathVariable Long idVisitante, @PathVariable Long limiteDeJogos) {
		return jogoService.findByJogoMandanteXVisitante(idCampeonato, idMandante, idVisitante, limiteDeJogos);
	}
	

}
