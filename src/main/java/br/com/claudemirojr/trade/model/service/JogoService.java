package br.com.claudemirojr.trade.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.claudemirojr.trade.dto.JogoAnaliseResponseDto;
import br.com.claudemirojr.trade.dto.JogoDadosResponseDto;
import br.com.claudemirojr.trade.dto.JogoDto;
import br.com.claudemirojr.trade.dto.JogoResponseDto;
import br.com.claudemirojr.trade.model.repository.IJogoPartida;
import br.com.claudemirojr.trade.model.repository.IJogoRodadas;

public interface JogoService {

	public JogoResponseDto criar(JogoDto jogoCriarDto);

	public JogoResponseDto atualizar(Long id, JogoDto jogoAtualizarDto);

	public void delete(Long id);

	public Page<JogoResponseDto> findAll(Pageable pageable);

	public Page<JogoResponseDto> findAllIdMaiorIgual(Long id, Pageable pageable);
	
	public Page<JogoResponseDto> findAllPorIdOrNome(String valor, Pageable pageable);
	

	public JogoResponseDto findById(Long id);

	public JogoAnaliseResponseDto findByAnaliseMandanteXVisitante(Long idCampeonado, Long idMandante, Long idVisitante, Long limiteDeJogos);

	public JogoDadosResponseDto findByJogoMandanteXVisitante(Long idCampeonado, Long idMandante, Long idVisitante, Long limiteDeJogos);
	
	public List<IJogoRodadas> obterQuantidadePorRodada (Long idCampeonato);
	
	public List<IJogoPartida> obterPartidaPorRodada (Long idCampeonato, Integer numeroRodada);


}
