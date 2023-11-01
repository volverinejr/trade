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
public class JogoDadosEquipeResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer numeroRodada;

	private Double totalGolHT;

	private Double totalGolFT;

	private Double totalEscanteioHT;

	private Double totalEscanteioFT;

}