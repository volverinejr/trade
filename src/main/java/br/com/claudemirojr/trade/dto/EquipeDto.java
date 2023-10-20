package br.com.claudemirojr.trade.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipeDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Length(min = 4, max = 100)
	private String nome;

}