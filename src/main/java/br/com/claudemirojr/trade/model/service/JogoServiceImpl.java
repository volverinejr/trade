package br.com.claudemirojr.trade.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.claudemirojr.trade.converter.JogoMapper;
import br.com.claudemirojr.trade.dto.JogoAnaliseResponseDto;
import br.com.claudemirojr.trade.dto.JogoAnaliseResponseEquipeDto;
import br.com.claudemirojr.trade.dto.JogoDadosResponseDto;
import br.com.claudemirojr.trade.dto.JogoDto;
import br.com.claudemirojr.trade.dto.JogoResponseDto;
import br.com.claudemirojr.trade.exception.ResourceNotFoundException;
import br.com.claudemirojr.trade.exception.ResourceServiceValidationException;
import br.com.claudemirojr.trade.model.entity.Jogo;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;
import br.com.claudemirojr.trade.model.repository.EquipeRepository;
import br.com.claudemirojr.trade.model.repository.IJogoPartida;
import br.com.claudemirojr.trade.model.repository.IJogoRodadas;
import br.com.claudemirojr.trade.model.repository.JogoRepository;
import br.com.claudemirojr.trade.util.Paginacao;

@Service
public class JogoServiceImpl implements JogoService {

	@Autowired
	private JogoRepository jogoRepository;

	@Autowired
	private CampeonatoRepository campeonatoRepository;

	@Autowired
	private EquipeRepository eqiupeRepository;
	
	@Autowired
	private Paginacao paginacao;
	
	@Autowired
	private JogoMapper jogoMapper;

	private final String MSG_ENTIDADE_NAO_EXISTE = "Jogo não encontrado para id %d";
	private final String MSG_CAMPEONATO_NAO_EXISTE = "Campeonato não encontrado para id %d";
	private final String MSG_EQUIPE_NAO_EXISTE = "Equipe não encontrada para id %d";
	
	private final String GREEN = "success";
	private final String RED = "danger";
	private final String EMPATE = "warning";

	
	private JogoResponseDto convertToJogoResponseDto(Jogo entity) {
		return jogoMapper.toResponseDto(entity);
		//return ModelMaperConverter.parseObject(entity, JogoResponseDto.class);
	}

	@Override
	@Transactional(readOnly = false)
	@CacheEvict(value = "trade_jogoCache", allEntries = true)
	public JogoResponseDto criar(JogoDto jogoCriarDto) {

		var idCampeonado = jogoCriarDto.getCampeonato().getId();
		var idMandante = jogoCriarDto.getEqpMandante().getId();
		var idVisitante = jogoCriarDto.getEqpVisitante().getId();

		campeonatoRepository.findById(idCampeonado).orElseThrow(
				() -> new ResourceNotFoundException(String.format(MSG_CAMPEONATO_NAO_EXISTE, idCampeonado)));

		var equipeMandante = eqiupeRepository.findById(idMandante)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_EQUIPE_NAO_EXISTE, idMandante)));

		var equipeVisitante = eqiupeRepository.findById(idVisitante)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_EQUIPE_NAO_EXISTE, idVisitante)));

		if (equipeMandante.equals(equipeVisitante)) {
			throw new ResourceServiceValidationException(
					String.format("Equipe visitante;tem que ser diferente da equipe mandante"));
		}

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

		var idCampeonado = jogoAtualizarDto.getCampeonato().getId();
		var idMandante = jogoAtualizarDto.getEqpMandante().getId();
		var idVisitante = jogoAtualizarDto.getEqpVisitante().getId();

		campeonatoRepository.findById(idCampeonado).orElseThrow(
				() -> new ResourceNotFoundException(String.format(MSG_CAMPEONATO_NAO_EXISTE, idCampeonado)));

		var equipeMandante = eqiupeRepository.findById(idMandante)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_EQUIPE_NAO_EXISTE, idMandante)));

		var equipeVisitante = eqiupeRepository.findById(idVisitante)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_EQUIPE_NAO_EXISTE, idVisitante)));

		if (equipeMandante.equals(equipeVisitante)) {
			throw new ResourceServiceValidationException(
					String.format("Equipe mandante tem que ser diferente da equipe visitante"));
		}

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
	public Page<JogoResponseDto> findAll(Pageable pageable) {
		pageable = paginacao.getPageable(pageable);

		var page = jogoRepository.findAll(pageable);

		return page.map(this::convertToJogoResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "trade_jogoCache")
	public Page<JogoResponseDto> findAllIdMaiorIgual(Long id, Pageable pageable) {
		pageable = paginacao.getPageable(pageable);

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
	public Page<JogoResponseDto> findAllPorIdOrNome(String valor, Pageable pageable) {
	
		pageable = paginacao.getPageable(pageable);
		
		Page<Jogo> page;
		
		if(paginacao.isStringNumeric(valor))
			
			page = jogoRepository.findByIdGreaterThanEqual(Long.parseLong(valor), pageable);
		
		else {
			//TODO: Fazer a busca por nome.
			page = jogoRepository.findByCampeonatoNomeContainingIgnoreCase(valor, pageable);
		}
		
		
		
		return page.map(this::convertToJogoResponseDto);
	}

	

	@Override
	@Transactional(readOnly = true)
	public JogoAnaliseResponseDto findByAnaliseMandanteXVisitante(Long idCampeonado, Long idMandante,
			Long idVisitante, Long limiteDeJogos) {
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

		var analisado = jogoRepository.findAnaliseMandante(idCampeonado, idMandante, limiteDeJogos);
		if (analisado.isPresent()) {
			mandante.setMarcouMediaEscanteioHT(analisado.get().getMarcouMediaEscanteioHT());
			mandante.setMarcouMediaEscanteioFT(analisado.get().getMarcouMediaEscanteioFT());
			mandante.setSofreuMediaEscanteioHT(analisado.get().getSofreuMediaEscanteioHT());
			mandante.setSofreuMediaEscanteioFT(analisado.get().getSofreuMediaEscanteioFT());
			mandante.setMediaEscanteioJogo(analisado.get().getMediaEscanteioJogo());
			mandante.setResultEscanteioHT(analisado.get().getResultEscanteioHT());
			mandante.setResultEscanteioFT(analisado.get().getResultEscanteioFT());
			mandante.setMediaGolJogoHT(analisado.get().getMediaGolJogoHT());
			mandante.setMediaGolJogoFT(analisado.get().getMediaGolJogoFT());

			jogoAnaliseResponseDto.setAnaliseMandante(mandante);
		}

		analisado = jogoRepository.findAnaliseVisitante(idCampeonado, idVisitante, limiteDeJogos);
		if (analisado.isPresent()) {
			visitante.setMarcouMediaEscanteioHT(analisado.get().getMarcouMediaEscanteioHT());
			visitante.setMarcouMediaEscanteioFT(analisado.get().getMarcouMediaEscanteioFT());
			visitante.setSofreuMediaEscanteioHT(analisado.get().getSofreuMediaEscanteioHT());
			visitante.setSofreuMediaEscanteioFT(analisado.get().getSofreuMediaEscanteioFT());
			visitante.setMediaEscanteioJogo(analisado.get().getMediaEscanteioJogo());
			visitante.setResultEscanteioHT(analisado.get().getResultEscanteioHT());
			visitante.setResultEscanteioFT(analisado.get().getResultEscanteioFT());
			visitante.setMediaGolJogoHT(analisado.get().getMediaGolJogoHT());
			visitante.setMediaGolJogoFT(analisado.get().getMediaGolJogoFT());

			jogoAnaliseResponseDto.setAnaliseVisitante(visitante);
		}
		
		
		//Mandante
		var jogosMandante = jogoRepository.findJogosMandante(idCampeonado, idMandante, limiteDeJogos);
		jogoAnaliseResponseDto.setJogosMandante(jogosMandante);
		
		var listGreen = jogosMandante.stream()
                .filter(IJogoDados -> IJogoDados.getResultGolFT().equals(this.GREEN))
                .toList();
		
		var listRed = jogosMandante.stream()
                .filter(IJogoDados -> IJogoDados.getResultGolFT().equals(this.RED))
                .toList();
		
		var listEmpate = jogosMandante.stream()
                .filter(IJogoDados -> IJogoDados.getResultGolFT().equals(this.EMPATE))
                .toList();
		
		
		ArrayList<String> palpitesMandante = new ArrayList<>();
		palpitesMandante.add( String.format( "Vitória: %.2f%%", this.regraDeTresSimples(jogosMandante.size(), listGreen.size())) );
		palpitesMandante.add( String.format( "Empate: %.2f%%", this.regraDeTresSimples(jogosMandante.size(), listEmpate.size())) );
		palpitesMandante.add( String.format( "Derrota: %.2f%%", this.regraDeTresSimples(jogosMandante.size(), listRed.size())) );
		jogoAnaliseResponseDto.setEquipeMandanteMercadoResultadoPalpite(palpitesMandante);
		
		
		
		//escanteio
		listGreen = jogosMandante.stream()
                .filter(IJogoDados -> IJogoDados.getResultEscanteioFT().equals(this.GREEN))
                .toList();
		
		listRed = jogosMandante.stream()
                .filter(IJogoDados -> IJogoDados.getResultEscanteioFT().equals(this.RED))
                .toList();
		
		listEmpate = jogosMandante.stream()
                .filter(IJogoDados -> IJogoDados.getResultEscanteioFT().equals(this.EMPATE))
                .toList();

		ArrayList<String> palpitesMandanteEscanteio = new ArrayList<>();
		palpitesMandanteEscanteio.add( String.format( "Vitória: %.2f%%", this.regraDeTresSimples(jogosMandante.size(), listGreen.size())) );
		palpitesMandanteEscanteio.add( String.format( "Empate: %.2f%%", this.regraDeTresSimples(jogosMandante.size(), listEmpate.size())) );
		palpitesMandanteEscanteio.add( String.format( "Derrota: %.2f%%", this.regraDeTresSimples(jogosMandante.size(), listRed.size())) );
		jogoAnaliseResponseDto.setEquipeMandanteMercadoEscanteioPalpite(palpitesMandanteEscanteio);
		
		
		
		
		//Visitante
		var jogosVisitante = jogoRepository.findJogosVisitante(idCampeonado, idVisitante, limiteDeJogos);
		jogoAnaliseResponseDto.setJogosVisitante(jogosVisitante);

		listGreen = jogosVisitante.stream()
                .filter(IJogoDados -> IJogoDados.getResultGolFT().equals(this.GREEN))
                .toList();
		
		listRed = jogosVisitante.stream()
                .filter(IJogoDados -> IJogoDados.getResultGolFT().equals(this.RED))
                .toList();
		
		listEmpate = jogosVisitante.stream()
                .filter(IJogoDados -> IJogoDados.getResultGolFT().equals(this.EMPATE))
                .toList();

		ArrayList<String> palpitesVisitante = new ArrayList<>();
		palpitesVisitante.add( String.format( "Vitória: %.2f%%", this.regraDeTresSimples(jogosVisitante.size(), listGreen.size())) );
		palpitesVisitante.add( String.format( "Empate: %.2f%%", this.regraDeTresSimples(jogosVisitante.size(), listEmpate.size())) );
		palpitesVisitante.add( String.format( "Derrota: %.2f%%", this.regraDeTresSimples(jogosVisitante.size(), listRed.size())) );
		jogoAnaliseResponseDto.setEquipeVisitanteMercadoResultadoPalpite(palpitesVisitante);
		

		//escanteio
		listGreen = jogosVisitante.stream()
                .filter(IJogoDados -> IJogoDados.getResultEscanteioFT().equals(this.GREEN))
                .toList();
		
		listRed = jogosVisitante.stream()
                .filter(IJogoDados -> IJogoDados.getResultEscanteioFT().equals(this.RED))
                .toList();
		
		listEmpate = jogosVisitante.stream()
                .filter(IJogoDados -> IJogoDados.getResultEscanteioFT().equals(this.EMPATE))
                .toList();
		
		
		ArrayList<String> palpitesVisitanteEscanteio = new ArrayList<>();
		palpitesVisitanteEscanteio.add( String.format( "Vitória: %.2f%%", this.regraDeTresSimples(jogosVisitante.size(), listGreen.size())) );
		palpitesVisitanteEscanteio.add( String.format( "Empate: %.2f%%", this.regraDeTresSimples(jogosVisitante.size(), listEmpate.size())) );
		palpitesVisitanteEscanteio.add( String.format( "Derrota: %.2f%%", this.regraDeTresSimples(jogosVisitante.size(), listRed.size())) );
		jogoAnaliseResponseDto.setEquipeVisitanteMercadoEscanteioPalpite(palpitesVisitanteEscanteio);
		
		
		
		//resultado - lista dos melhores
		jogoAnaliseResponseDto.setResultadoMelhorMandante( jogoRepository.resultadoMelhorMandante(idCampeonado) );
		jogoAnaliseResponseDto.setResultadoMelhorVisitante( jogoRepository.resultadoMelhorVisitante(idCampeonado) );
		
		//resultado - lista dos melhores escanyeios
		jogoAnaliseResponseDto.setResultadoMelhorMandanteEscanteio( jogoRepository.resultadoMelhorMandanteEscanteio(idCampeonado) );
		jogoAnaliseResponseDto.setResultadoMelhorVisitanteEscanteio( jogoRepository.resultadoMelhorVisitanteEscanteio(idCampeonado) );
		

		return jogoAnaliseResponseDto;
	}
	
	
	
	private Double regraDeTresSimples(Integer valorTotal, Integer valorX) {
		return Double.valueOf( (valorX * 100.00)/valorTotal );
	}

	@Override
	@Transactional(readOnly = true)
	public JogoDadosResponseDto findByJogoMandanteXVisitante(Long idCampeonado, Long idMandante, Long idVisitante, Long limiteDeJogos) {
		var campeonato = campeonatoRepository.findById(idCampeonado).orElseThrow(
				() -> new ResourceNotFoundException(String.format(MSG_CAMPEONATO_NAO_EXISTE, idCampeonado)));

		var equipeMandante = eqiupeRepository.findById(idMandante)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_EQUIPE_NAO_EXISTE, idMandante)));

		var equipeVisitante = eqiupeRepository.findById(idVisitante)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_EQUIPE_NAO_EXISTE, idVisitante)));

		JogoDadosResponseDto jogoDadosResponseDto = new JogoDadosResponseDto();
		jogoDadosResponseDto.setCampeonato(campeonato);

		jogoDadosResponseDto.setEquipeMandante(equipeMandante);
		var jogosMandante = jogoRepository.findJogosMandante(idCampeonado, idMandante, limiteDeJogos);
		jogoDadosResponseDto.setJogoMandante(jogosMandante);

		jogoDadosResponseDto.setEquipeVisitante(equipeVisitante);
		var jogosVisitante = jogoRepository.findJogosVisitante(idCampeonado, idVisitante, limiteDeJogos);
		jogoDadosResponseDto.setJogoVisitante(jogosVisitante);

		return jogoDadosResponseDto;
	}
	
	public List<IJogoRodadas> obterQuantidadePorRodada(Long idCampeonato) {
        return jogoRepository.FindNumeroRodadas(idCampeonato);
    }
	
	public List<IJogoPartida> obterPartidaPorRodada(Long idCampeonato , Integer numeroRodada) {
        
		return jogoRepository.FindPartidaPorRodada(idCampeonato, numeroRodada);
    }

}