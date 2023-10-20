package br.com.claudemirojr.trade.dto;

import java.io.Serializable;

import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.entity.Equipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JogoEquipeResultResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private Campeonato campeonato;

	private Integer numeroRodada;

	// ===== MANDANTE =====
	private Equipe eqpMandante;

	private Integer eqpMandantePrimeitoTempoTotalGol;

	private Integer eqpMandantePrimeitoTempoTotalEscanteio;

	private Integer eqpMandanteSegundoTempoTotalGol;

	private Integer eqpMandanteSegundoTempoTotalEscanteio;
	

	// ===== VISITANTE =====
	private Equipe eqpVisitante;

	private Integer eqpVisitantePrimeitoTempoTotalGol;

	private Integer eqpVisitantePrimeitoTempoTotalEscanteio;

	private Integer eqpVisitanteSegundoTempoTotalGol;

	private Integer eqpVisitanteSegundoTempoTotalEscanteio;
	
	// ===== RESULTADO =====
	private Integer mandanteResultPrimeitoTempoGol;

	private Integer mandanteResultPrimeitoTempoEscanteio;

	private Integer mandanteResultSegundoTempoGol;

	private Integer mandanteResultSegundoTempoEscanteio;

	private Integer mandanteResultGol;

	private Integer mandanteResultEscanteio;
	
}