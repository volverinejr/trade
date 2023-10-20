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
public class JogoAnaliseResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Campeonato campeonato;

	private Equipe equipeMandante;
	
	private JogoAnaliseResponseEquipeDto analiseMandante;

	private Equipe equipeVisitante;

	private JogoAnaliseResponseEquipeDto analiseVisitante;

}