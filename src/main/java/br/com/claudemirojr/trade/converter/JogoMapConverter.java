package br.com.claudemirojr.trade.converter;

import org.mapstruct.Mapper;

import br.com.claudemirojr.trade.dto.JogoDto;
import br.com.claudemirojr.trade.dto.JogoResponseDto;
import br.com.claudemirojr.trade.model.entity.Jogo;

@Mapper(componentModel = "spring")
public interface JogoMapConverter {
	
	JogoDto toDto(Jogo jogo);
	
	Jogo toJogo(JogoDto jogoDto);
	
	JogoResponseDto toResponseDto(Jogo jogo);	
}
