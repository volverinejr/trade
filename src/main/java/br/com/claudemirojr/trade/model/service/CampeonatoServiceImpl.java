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

import br.com.claudemirojr.trade.converter.ModelMaperConverter;
import br.com.claudemirojr.trade.dto.CampeonatoDto;
import br.com.claudemirojr.trade.dto.CampeonatoResponseDto;
import br.com.claudemirojr.trade.exception.ResourceNotFoundException;
import br.com.claudemirojr.trade.model.ParamsRequestModel;
import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;

@Service
public class CampeonatoServiceImpl implements CampeonatoService {

	@Autowired
	private CampeonatoRepository campeonatoRepository;

	public String MSG_ENTIDADE_NAO_EXISTE = "Campeonato não encontrado para id %d";

	private CampeonatoResponseDto convertToCampeonatoResponseDto(Campeonato entity) {
		return ModelMaperConverter.parseObject(entity, CampeonatoResponseDto.class);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_campeonatoCache", allEntries = true)
	public CampeonatoResponseDto criar(CampeonatoDto campeonatoCriarDto) {
		var entity = ModelMaperConverter.parseObject(campeonatoCriarDto, Campeonato.class);

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
	public Page<CampeonatoResponseDto> findAll(ParamsRequestModel prm) {
		Pageable pageable = prm.toSpringPageRequest();

		var page = campeonatoRepository.findAll(pageable);

		return page.map(this::convertToCampeonatoResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_campeonatoCache")
	public Page<CampeonatoResponseDto> findAllIdMaiorIgual(Long id, ParamsRequestModel prm) {
		Pageable pageable = prm.toSpringPageRequest();

		var page = campeonatoRepository.findByIdGreaterThanEqual(id, pageable);

		return page.map(this::convertToCampeonatoResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_campeonatoCache")
	public Page<CampeonatoResponseDto> findAllNomeContem(String nome, ParamsRequestModel prm) {
		Pageable pageable = prm.toSpringPageRequest();

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

}