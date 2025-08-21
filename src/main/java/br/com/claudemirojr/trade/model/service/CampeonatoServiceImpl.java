package br.com.claudemirojr.trade.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.claudemirojr.trade.converter.CampeonatoMapper;
import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;
import br.com.claudemirojr.trade.exception.ResourceNotFoundException;
import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;
import br.com.claudemirojr.trade.util.Paginacao;

@Service
public class CampeonatoServiceImpl implements CampeonatoService {

	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private Paginacao paginacao;
	
	@Autowired
	private CampeonatoMapper campeonatoMapper;

	public String MSG_ENTIDADE_NAO_EXISTE = "Campeonato não encontrado para id %d";

	private CampeonatoResponseDto convertToCampeonatoResponseDto(Campeonato entity) {
		return campeonatoMapper.toResponseDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_campeonatoCache", allEntries = true)
	public CampeonatoResponseDto criar(CampeonatoDto campeonatoCriarDto) {
		var entity = campeonatoMapper.toCampeonato(campeonatoCriarDto);

		var campeonato = campeonatoRepository.save(entity);

		return convertToCampeonatoResponseDto(campeonato);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_campeonatoCache", allEntries = true)
	public CampeonatoResponseDto atualizar(Long id, CampeonatoDto campeonatoAtualizarDto) {
		var entity = campeonatoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));

		entity.Atualizar(campeonatoAtualizarDto.getNome(), campeonatoAtualizarDto.getDescricao(),
				campeonatoAtualizarDto.getAtivo());

		var campeonato = campeonatoRepository.save(entity);

		return convertToCampeonatoResponseDto(campeonato);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_campeonatoCache", allEntries = true)
	public void delete(Long id) {
		var entity = campeonatoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));

		campeonatoRepository.delete(entity);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_campeonatoCache")
	public Page<CampeonatoResponseDto> findAll(Pageable pageable) {
		
		pageable = paginacao.getPageable(pageable);

		var page = campeonatoRepository.findAll(pageable);

		return page.map(this::convertToCampeonatoResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_campeonatoCache")
	public Page<CampeonatoResponseDto> findAllIdMaiorIgual(Long id, Pageable pageable) {
		pageable = paginacao.getPageable(pageable);

		var page = campeonatoRepository.findByIdGreaterThanEqual(id, pageable);

		return page.map(this::convertToCampeonatoResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_campeonatoCache")
	public Page<CampeonatoResponseDto> findAllNomeContem(String nome, Pageable pageable) {
		pageable = paginacao.getPageable(pageable);

		var page = campeonatoRepository.findByNomeIgnoreCaseContaining(nome, pageable);

		return page.map(this::convertToCampeonatoResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_campeonatoCache")
	public CampeonatoResponseDto findById(Long id) {
		var entity = campeonatoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));

		return convertToCampeonatoResponseDto(entity);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_campeonatoCache")
	public List<CampeonatoResponseDto> findByAtivo(Boolean ativo) {
		var registros = campeonatoRepository.findByAtivo(ativo);

		return registros.stream().map(this::convertToCampeonatoResponseDto).collect(Collectors.toList());
	}
	
	@Override
	public Page<CampeonatoResponseDto> findAllPorIdOrNome(String valor, Pageable pageable) {
	
		pageable = paginacao.getPageable(pageable);
		
		Page<Campeonato> page;
		
		if(paginacao.isStringNumeric(valor))
			
			page = campeonatoRepository.findByIdGreaterThanEqual(Long.parseLong(valor), pageable);
		else {
			page= campeonatoRepository.findByNomeIgnoreCaseContaining(valor, pageable);
		}
		
		
		return page.map(this::convertToCampeonatoResponseDto);
	}


}