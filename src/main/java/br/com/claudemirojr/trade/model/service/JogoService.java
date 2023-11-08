package br.com.claudemirojr.trade.model.service;

import org.springframework.data.domain.Page;

import br.com.claudemirojr.trade.dto.JogoAnaliseResponseDto;
import br.com.claudemirojr.trade.dto.JogoDadosResponseDto;
import br.com.claudemirojr.trade.dto.JogoDto;
import br.com.claudemirojr.trade.dto.JogoResponseDto;
import br.com.claudemirojr.trade.model.ParamsRequestModel;

public interface JogoService {

	public JogoResponseDto criar(JogoDto jogoCriarDto);

	public JogoResponseDto atualizar(Long id, JogoDto jogoAtualizarDto);

	public void delete(Long id);

	public Page<JogoResponseDto> findAll(ParamsRequestModel prm);

	public Page<JogoResponseDto> findAllIdMaiorIgual(Long id, ParamsRequestModel prm);

	public JogoResponseDto findById(Long id);

	public JogoAnaliseResponseDto findByAnaliseMandanteXVisitante(Long idCampeonado, Long idMandante, Long idVisitante, Long limiteDeJogos);

	public JogoDadosResponseDto findByJogoMandanteXVisitante(Long idCampeonado, Long idMandante, Long idVisitante, Long limiteDeJogos);

}
