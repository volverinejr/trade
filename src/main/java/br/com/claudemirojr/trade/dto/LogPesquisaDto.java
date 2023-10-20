package br.com.claudemirojr.trade.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogPesquisaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private String servico;

	@NotNull
	private String usuario;

	@NotNull
	private String className;

	@NotNull
	private String metodo;

	@NotNull
	private String argumento;

	@NotNull
	private String resultado;
}