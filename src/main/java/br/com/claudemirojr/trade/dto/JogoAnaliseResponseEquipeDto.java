package br.com.claudemirojr.trade.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JogoAnaliseResponseEquipeDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Double marcouMediaEscanteioHT;

	private Double marcouMediaEscanteioFT;

	private Double mediaEscanteioJogo;

	private Double resultEscanteioHT;

	private Double resultEscanteioFT;

	private Double mediaGolJogoHT;

	private Double mediaGolJogoFT;

}