package br.com.claudemirojr.trade.controller;

import java.util.List;
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

import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;
import br.com.claudemirojr.trade.model.ParamsRequestModel;
import br.com.claudemirojr.trade.model.service.CampeonatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Campeonato", description = "Endpoints para gerenciamento dos campeonatos")
@RestController
@RequestMapping("${url.base}/campeonato/v1")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CAMPEONATO_ALL')")
public class CampeonatoController {

	@Autowired
	private CampeonatoService campeonatoService;

	@Operation(summary = "Insere um novo campeonato")
	@PostMapping
	public ResponseEntity<CampeonatoResponseDto> create(@RequestBody @Valid CampeonatoDto campeonatoDto) {
		CampeonatoResponseDto novo = campeonatoService.criar(campeonatoDto);

		return new ResponseEntity<>(novo, HttpStatus.CREATED);
	}

	@Operation(summary = "Atualiza os dados de um campeonato")
	@PutMapping("/{id}")
	public ResponseEntity<CampeonatoResponseDto> update(@PathVariable("id") Long id, @RequestBody @Valid CampeonatoDto campeonatoDto) {
		CampeonatoResponseDto existe = campeonatoService.atualizar(id, campeonatoDto);

		return new ResponseEntity<>(existe, HttpStatus.OK);
	}

	@Operation(summary = "Exclui um campeonato")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		campeonatoService.delete(id);

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Busca os dados de todos os campeonatos")
	@GetMapping
	public ResponseEntity<?> findAll(@RequestParam Map<String, String> params) {
		ParamsRequestModel prm = new ParamsRequestModel(params);

		Page<CampeonatoResponseDto> registros = campeonatoService.findAll(prm);

		return ResponseEntity.ok(registros);
	}

	@Operation(summary = "Filtra os dados dos campeonatos pelo id")
	@GetMapping("/search-id-maior-igual/{id}")
	public ResponseEntity<?> findAllIdMaiorIgual(@PathVariable Long id, @RequestParam Map<String, String> params) {
		ParamsRequestModel prm = new ParamsRequestModel(params);

		Page<CampeonatoResponseDto> registros = campeonatoService.findAllIdMaiorIgual(id, prm);

		return ResponseEntity.ok(registros);
	}

	@Operation(summary = "Filtra os dados dos campeonatos pelo nome")
	@GetMapping("/search-nome/{nome}")
	public ResponseEntity<?> findAllNomeContem(@PathVariable String nome, @RequestParam Map<String, String> params) {
		ParamsRequestModel prm = new ParamsRequestModel(params);

		Page<CampeonatoResponseDto> registros = campeonatoService.findAllNomeContem(nome, prm);

		return ResponseEntity.ok(registros);
	}
	
	
	
	@Operation(summary = "Busca os dados dos campeonatos que estão ativos")
	@GetMapping("/search-ativo")
	public ResponseEntity<?> carregarCampeonatosAtivos() {
		List<CampeonatoResponseDto> registros = campeonatoService.findByAtivo(true);

		return ResponseEntity.ok(registros);
	}
	

	@Operation(summary = "Busca os dados de um campeonato pelo id")
	@GetMapping("/{id}")
	public CampeonatoResponseDto findById(@PathVariable("id") Long id) {
		return campeonatoService.findById(id);
	}

}
