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
public class CampeonatoResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String nome;

	private String descricao;

	private Boolean ativo;

}