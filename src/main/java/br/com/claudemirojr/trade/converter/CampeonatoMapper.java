package br.com.claudemirojr.trade.converter;

import org.mapstruct.Mapper;

import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;
import br.com.claudemirojr.trade.model.entity.Campeonato;

@Mapper(componentModel = "spring")
public interface CampeonatoMapper {
	CampeonatoDto toDto(Campeonato campeonato);
	
	Campeonato toCampeonato(CampeonatoDto campeonatoDto);
	
	Campeonato toCampeonato(CampeonatoResponseDto campeonatoResponseDto);
	
	CampeonatoResponseDto toResponseDto(Campeonato campeonato);

}
