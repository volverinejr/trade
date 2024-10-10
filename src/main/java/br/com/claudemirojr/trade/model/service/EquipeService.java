package br.com.claudemirojr.trade.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;

public interface EquipeService {

	public EquipeResponseDto criar(EquipeDto equipeCriarDto);

	public EquipeResponseDto atualizar(Long id, EquipeDto equipeAtualizarDto);

	public void delete(Long id);

	public Page<EquipeResponseDto> findAll(Pageable pageable);

	public Page<EquipeResponseDto> findAllIdMaiorIgual(Long id, Pageable pageable);

	public Page<EquipeResponseDto> findAllNomeContem(String nome, Pageable pageable);

	public EquipeResponseDto findById(Long id);

	public List<EquipeResponseDto> findAllOrderNome();

}
