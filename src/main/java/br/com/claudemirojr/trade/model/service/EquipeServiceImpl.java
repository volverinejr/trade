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
import br.com.claudemirojr.trade.dto.EquipeDto;
import br.com.claudemirojr.trade.dto.EquipeResponseDto;
import br.com.claudemirojr.trade.exception.ResourceNotFoundException;
import br.com.claudemirojr.trade.model.ParamsRequestModel;
import br.com.claudemirojr.trade.model.entity.Equipe;
import br.com.claudemirojr.trade.model.repository.EquipeRepository;
import br.com.claudemirojr.trade.util.Paginacao;

@Service
public class EquipeServiceImpl implements EquipeService {

	@Autowired
	private EquipeRepository equipeRepository;
	
	@Autowired
	private Paginacao paginacao;

	public String MSG_ENTIDADE_NAO_EXISTE = "Equipe não encontrada para id %d";

	private EquipeResponseDto convertToEquipeResponseDto(Equipe entity) {
		return ModelMaperConverter.parseObject(entity, EquipeResponseDto.class);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_equipeCache", allEntries = true)
	public EquipeResponseDto criar(EquipeDto equipeCriarDto) {
		var entity = ModelMaperConverter.parseObject(equipeCriarDto, Equipe.class);

		var equipe = equipeRepository.save(entity);

		return convertToEquipeResponseDto(equipe);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_equipeCache", allEntries = true)
	public EquipeResponseDto atualizar(Long id, EquipeDto equipeAtualizarDto) {
		var entity = equipeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));

		entity.Atualizar(equipeAtualizarDto.getNome());

		var equipe = equipeRepository.save(entity);

		return convertToEquipeResponseDto(equipe);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_equipeCache", allEntries = true)
	public void delete(Long id) {
		var entity = equipeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));

		equipeRepository.delete(entity);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_equipeCache")
	public Page<EquipeResponseDto> findAll(Pageable pageable) {

		pageable = paginacao.getPageable(pageable);

		var page = equipeRepository.findAll(pageable);

		return page.map(this::convertToEquipeResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_equipeCache")
	public Page<EquipeResponseDto> findAllIdMaiorIgual(Long id, Pageable pageable) {
		pageable = paginacao.getPageable(pageable);

		var page = equipeRepository.findByIdGreaterThanEqual(id, pageable);

		return page.map(this::convertToEquipeResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_equipeCache")
	public Page<EquipeResponseDto> findAllNomeContem(String nome,Pageable pageable) {
		pageable = paginacao.getPageable(pageable);

		var page = equipeRepository.findByNomeIgnoreCaseContaining(nome, pageable);

		return page.map(this::convertToEquipeResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_equipeCache")
	public EquipeResponseDto findById(Long id) {
		var entity = equipeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));

		return convertToEquipeResponseDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_equipeCache", allEntries = true)
	public List<EquipeResponseDto> findAllOrderNome() {
		var registros = equipeRepository.findAllByOrderByNomeAsc();

		return registros.stream().map(this::convertToEquipeResponseDto).collect(Collectors.toList());
	}

}