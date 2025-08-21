package br.com.claudemirojr.trade.converter;

import org.mapstruct.Mapper;

import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;
import br.com.claudemirojr.trade.model.entity.Equipe;

@Mapper(componentModel = "spring")
public interface EquipeMapConverter {
	EquipeDto toDto(Equipe equipe);
	
	Equipe toEquipe(EquipeDto equipeDto);
		
	Equipe toEquipe(EquipeResponseDto equipeResponseDto);

	EquipeResponseDto toResponseDto(Equipe equipe);
}
