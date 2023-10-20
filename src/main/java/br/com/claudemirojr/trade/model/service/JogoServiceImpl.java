package br.com.claudemirojr.trade.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.claudemirojr.trade.converter.ModelMaperConverter;
import br.com.claudemirojr.trade.dto.JogoAnaliseResponseDto;
import br.com.claudemirojr.trade.dto.JogoAnaliseResponseEquipeDto;
import br.com.claudemirojr.trade.dto.JogoDto;
import br.com.claudemirojr.trade.dto.JogoResponseDto;
import br.com.claudemirojr.trade.exception.ResourceNotFoundException;
import br.com.claudemirojr.trade.model.ParamsRequestModel;
import br.com.claudemirojr.trade.model.entity.Jogo;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;
import br.com.claudemirojr.trade.model.repository.EquipeRepository;
import br.com.claudemirojr.trade.model.repository.JogoRepository;

@Service
public class JogoServiceImpl implements JogoService {

	@Autowired
	private JogoRepository jogoRepository;

	@Autowired
	private CampeonatoRepository campeonatoRepository;

	@Autowired
	private EquipeRepository eqiupeRepository;

	public String MSG_ENTIDADE_NAO_EXISTE = "Jogo não encontrado para id %d";
	public String MSG_CAMPEONATO_NAO_EXISTE = "Campeonato não encontrado para id %d";
	public String MSG_EQUIPE_NAO_EXISTE = "Equipe não encontrada para id %d";

	private JogoResponseDto convertToJogoResponseDto(Jogo entity) {
		return ModelMaperConverter.parseObject(entity, JogoResponseDto.class); 
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_jogoCache", allEntries = true)
	public JogoResponseDto criar(JogoDto jogoCriarDto) {
		var entity = new Jogo(jogoCriarDto.getCampeonato(), jogoCriarDto.getNumeroRodada(),

				jogoCriarDto.getEqpMandante(), jogoCriarDto.getEqpMandantePrimeitoTempoTotalGol(),
				jogoCriarDto.getEqpMandantePrimeitoTempoTotalEscanteio(),
				jogoCriarDto.getEqpMandanteSegundoTempoTotalGol(),
				jogoCriarDto.getEqpMandanteSegundoTempoTotalEscanteio(),

				jogoCriarDto.getEqpVisitante(), jogoCriarDto.getEqpVisitantePrimeitoTempoTotalGol(),
				jogoCriarDto.getEqpVisitantePrimeitoTempoTotalEscanteio(),
				jogoCriarDto.getEqpVisitanteSegundoTempoTotalGol(),
				jogoCriarDto.getEqpVisitanteSegundoTempoTotalEscanteio());

		var jogo = jogoRepository.save(entity);

		return convertToJogoResponseDto(jogo);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_jogoCache", allEntries = true)
	public JogoResponseDto atualizar(Long id, JogoDto jogoAtualizarDto) {
		var entity = jogoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));

		entity.Atualizar(jogoAtualizarDto.getCampeonato(), jogoAtualizarDto.getNumeroRodada(),

				jogoAtualizarDto.getEqpMandante(), jogoAtualizarDto.getEqpMandantePrimeitoTempoTotalGol(),
				jogoAtualizarDto.getEqpMandantePrimeitoTempoTotalEscanteio(),
				jogoAtualizarDto.getEqpMandanteSegundoTempoTotalGol(),
				jogoAtualizarDto.getEqpMandanteSegundoTempoTotalEscanteio(),

				jogoAtualizarDto.getEqpVisitante(), jogoAtualizarDto.getEqpVisitantePrimeitoTempoTotalGol(),
				jogoAtualizarDto.getEqpVisitantePrimeitoTempoTotalEscanteio(),
				jogoAtualizarDto.getEqpVisitanteSegundoTempoTotalGol(),
				jogoAtualizarDto.getEqpVisitanteSegundoTempoTotalEscanteio());

		var jogo = jogoRepository.save(entity);

		return convertToJogoResponseDto(jogo);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_jogoCache", allEntries = true)
	public void delete(Long id) {
		var entity = jogoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));

		jogoRepository.delete(entity);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_jogoCache")
	public Page<JogoResponseDto> findAll(ParamsRequestModel prm) {
		Pageable pageable = prm.toSpringPageRequest();

		var page = jogoRepository.findAll(pageable);

		return page.map(this::convertToJogoResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_jogoCache")
	public Page<JogoResponseDto> findAllIdMaiorIgual(Long id, ParamsRequestModel prm) {
		Pageable pageable = prm.toSpringPageRequest();

		var page = jogoRepository.findByIdGreaterThanEqual(id, pageable);

		return page.map(this::convertToJogoResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "trade_jogoCache")
	public JogoResponseDto findById(Long id) {
		var entity = jogoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_ENTIDADE_NAO_EXISTE, id)));
		
		return convertToJogoResponseDto(entity);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_jogoAnaliseMandanteVisitanteCache")
	public JogoAnaliseResponseDto findByAnaliseMandanteXVisitante(Long idCampeonado, Long idMandante,
			Long idVisitante) {
		var campeonato = campeonatoRepository.findById(idCampeonado).orElseThrow(
				() -> new ResourceNotFoundException(String.format(MSG_CAMPEONATO_NAO_EXISTE, idCampeonado)));

		var equipeMandante = eqiupeRepository.findById(idMandante)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_EQUIPE_NAO_EXISTE, idMandante)));

		var equipeVisitante = eqiupeRepository.findById(idVisitante)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_EQUIPE_NAO_EXISTE, idVisitante)));

		JogoAnaliseResponseDto jogoAnaliseResponseDto = new JogoAnaliseResponseDto();
		jogoAnaliseResponseDto.setCampeonato(campeonato);
		jogoAnaliseResponseDto.setEquipeMandante(equipeMandante);
		jogoAnaliseResponseDto.setEquipeVisitante(equipeVisitante);

		JogoAnaliseResponseEquipeDto mandante = new JogoAnaliseResponseEquipeDto();
		JogoAnaliseResponseEquipeDto visitante = new JogoAnaliseResponseEquipeDto();

		var analisado = jogoRepository.findAnaliseMandante(idCampeonado, idMandante);
		if (analisado.isPresent()) {
			mandante.setMarcouMediaEscanteioHT(analisado.get().getMarcouMediaEscanteioHT());
			mandante.setMarcouMediaEscanteioFT(analisado.get().getMarcouMediaEscanteioFT());
			mandante.setMediaEscanteioJogo(analisado.get().getMediaEscanteioJogo());
			mandante.setResultEscanteioHT(analisado.get().getResultEscanteioHT());
			mandante.setResultEscanteioFT(analisado.get().getResultEscanteioFT());
			mandante.setMediaGolJogoHT(analisado.get().getMediaGolJogoHT());
			mandante.setMediaGolJogoFT(analisado.get().getMediaGolJogoFT());

			jogoAnaliseResponseDto.setAnaliseMandante(mandante);
		}

		analisado = jogoRepository.findAnaliseVisitante(idCampeonado, idVisitante);
		if (analisado.isPresent()) {
			visitante.setMarcouMediaEscanteioHT(analisado.get().getMarcouMediaEscanteioHT());
			visitante.setMarcouMediaEscanteioFT(analisado.get().getMarcouMediaEscanteioFT());
			visitante.setMediaEscanteioJogo(analisado.get().getMediaEscanteioJogo());
			visitante.setResultEscanteioHT(analisado.get().getResultEscanteioHT());
			visitante.setResultEscanteioFT(analisado.get().getResultEscanteioFT());
			visitante.setMediaGolJogoHT(analisado.get().getMediaGolJogoHT());
			visitante.setMediaGolJogoFT(analisado.get().getMediaGolJogoFT());

			jogoAnaliseResponseDto.setAnaliseVisitante(visitante);
		}

		return jogoAnaliseResponseDto;
	}

}