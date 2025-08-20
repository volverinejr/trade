package br.com.claudemirojr.trade.converter;

import org.mapstruct.Mapper;

import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;
import br.com.claudemirojr.trade.model.entity.Equipe;

@Mapper(componentModel = "spring")
public interface EquipeMapper {
	EquipeDto toDto(Equipe equipe);
	
	Equipe toEquipe(EquipeDto equipeDto);
	
	EquipeResponseDto toResponseDto(Equipe equipe);
}
