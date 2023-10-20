package br.com.claudemirojr.trade.model.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;
import br.com.claudemirojr.trade.model.ParamsRequestModel;

public interface EquipeService {

	public EquipeResponseDto criar(EquipeDto equipeCriarDto);

	public EquipeResponseDto atualizar(Long id, EquipeDto equipeAtualizarDto);

	public void delete(Long id);

	public Page<EquipeResponseDto> findAll(ParamsRequestModel prm);

	public Page<EquipeResponseDto> findAllIdMaiorIgual(Long id, ParamsRequestModel prm);

	public Page<EquipeResponseDto> findAllNomeContem(String nome, ParamsRequestModel prm);

	public EquipeResponseDto findById(Long id);

	public List<EquipeResponseDto> findAllOrderNome();

}
