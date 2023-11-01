package br.com.claudemirojr.trade.dto;

import java.io.Serializable;
import java.util.List;

import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.entity.Equipe;
import br.com.claudemirojr.trade.model.repository.IJogoDados;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JogoDadosResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Campeonato campeonato;

	private Equipe equipeMandante;

	private List<IJogoDados> jogoMandante;

	private Equipe equipeVisitante;

	private List<IJogoDados> jogoVisitante;

}