package br.com.claudemirojr.trade.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.entity.Equipe;
import br.com.claudemirojr.trade.model.entity.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

	Page<Jogo> findByIdGreaterThanEqual(Long id, Pageable pageable);
	
	List<Jogo> findTop5ByCampeonatoAndEqpMandanteOrderByNumeroRodadaDesc(Campeonato campeonato, Equipe eqpMandante);

	List<Jogo> findTop5ByCampeonatoAndEqpVisitanteOrderByNumeroRodadaDesc(Campeonato campeonato, Equipe eqpVisitante);
	
	
	
	@Query(value="""
		SELECT 	avg(a.eqp_mandante_primeito_tempo_total_escanteio) as marcouMediaEscanteioHT,
				avg(a.eqp_mandante_total_escanteio) as marcouMediaEscanteioFT,
		        avg( a.eqp_mandante_total_escanteio + a.eqp_visitante_total_escanteio ) as mediaEscanteioJogo,

				sum(
                case(a.mandante_result_primeito_tempo_escanteio)
					when 1 then 1
					else 0
				end) as resultEscanteioHT,
				
				sum(
                case(a.mandante_result_escanteio)
					when 1 then 1
					else 0
				end) as resultEscanteioFT,
					
				avg(a.eqp_mandante_primeito_tempo_total_gol + a.eqp_visitante_primeito_tempo_total_gol) as mediaGolJogoHT,
		        avg(a.eqp_mandante_total_gol + a.eqp_visitante_total_gol) as mediaGolJogoFT
		FROM jogo as a
		where a.campeonato_id = ?1
				and a.eqp_mandante_id = ?2
		order by numero_rodada desc 
		limit 5
			""", nativeQuery = true )	
	Optional<IJogoResultAnalise> findAnaliseMandante(Long idCampeonato, Long idMandante);
	
	
	@Query(value="""
			SELECT 	avg(a.eqp_visitante_primeito_tempo_total_escanteio) as marcouMediaEscanteioHT,
					avg(a.eqp_visitante_total_escanteio) as marcouMediaEscanteioFT,
			        avg( a.eqp_mandante_total_escanteio + a.eqp_visitante_total_escanteio ) as mediaEscanteioJogo,
			        
					sum(
                    case(a.mandante_result_primeito_tempo_escanteio)
						when -1 then 1
						else 0
					end) as resultEscanteioHT,
					
					sum(
                    case(a.mandante_result_escanteio)
						when -1 then 1
						else 0
					end) as resultEscanteioFT,
                    
					avg(a.eqp_mandante_primeito_tempo_total_gol + a.eqp_visitante_primeito_tempo_total_gol) as mediaGolJogoHT,
			        avg(a.eqp_mandante_total_gol + a.eqp_visitante_total_gol) as mediaGolJogoFT
			FROM jogo as a
			where a.campeonato_id = ?1
					and a.eqp_visitante_id = ?2
			order by numero_rodada desc 
			limit 5
				""", nativeQuery = true )	
		Optional<IJogoResultAnalise> findAnaliseVisitante(Long idCampeonato, Long idVisitante);
	
}