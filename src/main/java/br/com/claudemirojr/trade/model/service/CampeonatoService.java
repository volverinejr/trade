package br.com.claudemirojr.trade.model.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;
import br.com.claudemirojr.trade.model.ParamsRequestModel;

public interface CampeonatoService {

	public CampeonatoResponseDto criar(CampeonatoDto campeonatoCriarDto);

	public CampeonatoResponseDto atualizar(Long id, CampeonatoDto campeonatoAtualizarDto);

	public void delete(Long id);

	public Page<CampeonatoResponseDto> findAll(ParamsRequestModel prm);

	public Page<CampeonatoResponseDto> findAllIdMaiorIgual(Long id, ParamsRequestModel prm);

	public Page<CampeonatoResponseDto> findAllNomeContem(String nome, ParamsRequestModel prm);

	public CampeonatoResponseDto findById(Long id);
	
	public List<CampeonatoResponseDto> findByAtivo(Boolean ativo);

}
