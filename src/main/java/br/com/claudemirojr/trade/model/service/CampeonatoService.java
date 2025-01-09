package br.com.claudemirojr.trade.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;

public interface CampeonatoService {

	public CampeonatoResponseDto criar(CampeonatoDto campeonatoCriarDto);

	public CampeonatoResponseDto atualizar(Long id, CampeonatoDto campeonatoAtualizarDto);

	public void delete(Long id);

	public Page<CampeonatoResponseDto> findAll( Pageable pageable);

	public Page<CampeonatoResponseDto> findAllIdMaiorIgual(Long id,  Pageable pageable);

	public Page<CampeonatoResponseDto> findAllNomeContem(String nome,  Pageable pageable);

	public CampeonatoResponseDto findById(Long id);
	
	public List<CampeonatoResponseDto> findByAtivo(Boolean ativo);
	
	public Page<CampeonatoResponseDto> findAllPorIdOrNome(String valor, Pageable pageable);


}
