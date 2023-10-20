package br.com.claudemirojr.trade.dto;

import java.io.Serializable;

import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.entity.Equipe;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JogoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Campeonato campeonato;
	
	@NotNull
	@Min(1)
	private Integer numeroRodada;
	

	//===== MANDANTE =====
	@NotNull
	private Equipe eqpMandante;
	
	@NotNull
	@Min(0)
	private Integer eqpMandantePrimeitoTempoTotalGol;

	@NotNull
	@Min(0)
	private Integer eqpMandantePrimeitoTempoTotalEscanteio;

	@NotNull
	@Min(0)
	private Integer eqpMandanteSegundoTempoTotalGol;

	@NotNull
	@Min(0)
	private Integer eqpMandanteSegundoTempoTotalEscanteio;
	
	
	//===== VISITANTE =====
	@NotNull
	private Equipe eqpVisitante;

	@NotNull
	@Min(0)
	private Integer eqpVisitantePrimeitoTempoTotalGol;

	@NotNull
	@Min(0)
	private Integer eqpVisitantePrimeitoTempoTotalEscanteio;

	@NotNull
	@Min(0)
	private Integer eqpVisitanteSegundoTempoTotalGol;

	@NotNull
	@Min(0)
	private Integer eqpVisitanteSegundoTempoTotalEscanteio;
	

}