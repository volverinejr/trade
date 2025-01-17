package br.com.claudemirojr.trade.dto;

import java.io.Serializable;
import java.util.ArrayList;
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
public class JogoAnaliseResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Campeonato campeonato;

	//MANDANTE
	private Equipe equipeMandante;
	
	private JogoAnaliseResponseEquipeDto analiseMandante;

	private List<IJogoDados> jogosMandante;
	
	private ArrayList<String> equipeMandanteMercadoResultadoPalpite;

	private ArrayList<String> equipeMandanteMercadoEscanteioPalpite;

	//VISITANTE
	private Equipe equipeVisitante;

	private JogoAnaliseResponseEquipeDto analiseVisitante;

	private List<IJogoDados> jogosVisitante;
	
	private ArrayList<String> equipeVisitanteMercadoResultadoPalpite;
	
	private ArrayList<String> equipeVisitanteMercadoEscanteioPalpite;

}