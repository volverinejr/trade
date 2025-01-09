package br.com.claudemirojr.trade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;
import br.com.claudemirojr.trade.model.service.EquipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Equipe", description = "Endpoints para gerenciamento das equipes")
@RestController
@RequestMapping("${url.base}/equipe/v1")
@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_TRADE_ADMIN')")
public class EquipeController {

	@Autowired
	private EquipeService equipeService;

	@Operation(summary = "Insere uma nova equipe")
	@PostMapping
	public ResponseEntity<EquipeResponseDto> create(@RequestBody @Valid EquipeDto equipeDto) {
		EquipeResponseDto novo = equipeService.criar(equipeDto);

		return new ResponseEntity<>(novo, HttpStatus.CREATED);
	}

	@Operation(summary = "Atualiza os dados de uma equipe")
	@PutMapping("/{id}")
	public ResponseEntity<EquipeResponseDto> update(@PathVariable("id") Long id,
			@RequestBody @Valid EquipeDto equipeDto) {
		EquipeResponseDto existe = equipeService.atualizar(id, equipeDto);

		return new ResponseEntity<>(existe, HttpStatus.OK);
	}

	@Operation(summary = "Exclui uma equipe")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		equipeService.delete(id);

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Busca os dados de todas as equipes")
	@GetMapping
	public ResponseEntity<?> findAll(Pageable pageable) {
		

		Page<EquipeResponseDto> registros = equipeService.findAll(pageable);

		return ResponseEntity.ok(registros);
	}
	
	

	@Operation(summary = "Filtra os dados das equipes pelo id")
	@GetMapping("/search-id-maior-igual/{id}")
	public ResponseEntity<?> findAllIdMaiorIgual(@PathVariable Long id, Pageable pageable) {
		

		Page<EquipeResponseDto> registros = equipeService.findAllIdMaiorIgual(id, pageable);

		return ResponseEntity.ok(registros);
	}

	@Operation(summary = "Filtra os dados das equipes pelo nome")
	@GetMapping("/search-nome/{nome}")
	public ResponseEntity<?> findAllNomeContem(@PathVariable String nome, Pageable pageable) {
		

		Page<EquipeResponseDto> registros = equipeService.findAllNomeContem(nome, pageable);

		return ResponseEntity.ok(registros);
	}

	@Operation(summary = "Busca os dados de uma equipe pelo id")
	@GetMapping("/{id}")
	public EquipeResponseDto findByAuditId(@PathVariable("id") Long id) {
		return equipeService.findById(id);
	}

	@Operation(summary = "Busca todas as equipes ordenadas pelo nome")
	@GetMapping("/search-all")
	public ResponseEntity<?> carregarCampeonatosAtivos() {
		List<EquipeResponseDto> registros = equipeService.findAllOrderNome();

		return ResponseEntity.ok(registros);
	}
	
	
	@GetMapping("/filter/{valor}")
	public ResponseEntity<?> findAllFilter(@PathVariable("valor") String valor, Pageable pageable) {
		Page<EquipeResponseDto> registros = equipeService.findAllPorIdOrNome(valor, pageable);

		return ResponseEntity.ok(registros);
	}

}
