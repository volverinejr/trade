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
	
	//Page<Jogo> findByNomeIgnoreCaseContaining(String nome, Pageable pageable);


	List<Jogo> findTop5ByCampeonatoAndEqpMandanteOrderByNumeroRodadaDesc(Campeonato campeonato, Equipe eqpMandante);

	List<Jogo> findTop5ByCampeonatoAndEqpVisitanteOrderByNumeroRodadaDesc(Campeonato campeonato, Equipe eqpVisitante);
	
	
	
	@Query(value="""
			select 	avg(resultSub.marcouMediaEscanteioHT) as marcouMediaEscanteioHT,
					avg(resultSub.marcouMediaEscanteioFT) as marcouMediaEscanteioFT,
					
					avg(resultSub.sofreuMediaEscanteioHT) as sofreuMediaEscanteioHT,
					avg(resultSub.sofreuMediaEscanteioFT) as sofreuMediaEscanteioFT,
								        
			        avg(resultSub.mediaEscanteioJogo) as mediaEscanteioJogo,
			        
			        sum(resultSub.resultEscanteioHT) as resultEscanteioHT,
			        sum(resultSub.resultEscanteioFT) as resultEscanteioFT,
			        
			        avg(resultSub.mediaGolJogoHT) as mediaGolJogoHT,
			        avg(resultSub.mediaGolJogoFT) as mediaGolJogoFT
			        
			from (
				SELECT 	a.eqp_mandante_primeito_tempo_total_escanteio as marcouMediaEscanteioHT,
						a.eqp_mandante_total_escanteio as marcouMediaEscanteioFT,
						
						a.eqp_visitante_primeito_tempo_total_escanteio as sofreuMediaEscanteioHT,
						a.eqp_visitante_total_escanteio as sofreuMediaEscanteioFT,
						
						a.eqp_mandante_total_escanteio + a.eqp_visitante_total_escanteio as mediaEscanteioJogo,
						case(a.mandante_result_primeito_tempo_escanteio)
							when 1 then 1
							else 0
						end as resultEscanteioHT,
						
						case(a.mandante_result_escanteio)
							when 1 then 1
							else 0
						end as resultEscanteioFT,
							
						a.eqp_mandante_primeito_tempo_total_gol + a.eqp_visitante_primeito_tempo_total_gol as mediaGolJogoHT,
						a.eqp_mandante_total_gol + a.eqp_visitante_total_gol as mediaGolJogoFT
				FROM jogo as a
				where a.campeonato_id = ?1
						and a.eqp_mandante_id = ?2
				order by a.numero_rodada desc 
				limit ?3
			    ) as resultSub
			""", nativeQuery = true )	
	Optional<IJogoResultAnalise> findAnaliseMandante(Long idCampeonato, Long idMandante, Long limiteDeJogos);
	
	
	@Query(value="""
			select	avg(resultSub.marcouMediaEscanteioHT)  as marcouMediaEscanteioHT,
					avg(resultSub.marcouMediaEscanteioFT) as marcouMediaEscanteioFT,
					
					avg(resultSub.sofreuMediaEscanteioHT) as sofreuMediaEscanteioHT,
					avg(resultSub.sofreuMediaEscanteioFT) as sofreuMediaEscanteioFT,
					
			        avg(resultSub.mediaEscanteioJogo) as mediaEscanteioJogo,
			        
			        sum(resultSub.resultEscanteioHT) as resultEscanteioHT,
			        sum(resultSub.resultEscanteioFT) as resultEscanteioFT,
			        
			        avg(resultSub.mediaGolJogoHT) as mediaGolJogoHT,
			        avg(resultSub.mediaGolJogoFT) as mediaGolJogoFT
			from (
			SELECT 	a.eqp_visitante_primeito_tempo_total_escanteio as marcouMediaEscanteioHT,
					a.eqp_visitante_total_escanteio as marcouMediaEscanteioFT,
					
					a.eqp_mandante_primeito_tempo_total_escanteio as sofreuMediaEscanteioHT,
					a.eqp_mandante_total_escanteio as sofreuMediaEscanteioFT,
										
					a.eqp_mandante_total_escanteio + a.eqp_visitante_total_escanteio as mediaEscanteioJogo,
					
					case(a.mandante_result_primeito_tempo_escanteio)
						when -1 then 1
						else 0
					end as resultEscanteioHT,
					
					case(a.mandante_result_escanteio)
						when -1 then 1
						else 0
					end as resultEscanteioFT,
					
					a.eqp_mandante_primeito_tempo_total_gol + a.eqp_visitante_primeito_tempo_total_gol as mediaGolJogoHT,
					a.eqp_mandante_total_gol + a.eqp_visitante_total_gol as mediaGolJogoFT
			FROM jogo as a
			where a.campeonato_id = ?1
					and a.eqp_visitante_id = ?2
			order by a.numero_rodada desc 
			limit ?3
			) as resultSub				
			""", nativeQuery = true )	
		Optional<IJogoResultAnalise> findAnaliseVisitante(Long idCampeonato, Long idVisitante, Long limiteDeJogos);
	
	
	
	
	@Query(value="""
			SELECT 	a.numero_rodada as numeroRodada,
					visitante.nome as equipe,

					( a.eqp_mandante_primeito_tempo_total_gol + a.eqp_visitante_primeito_tempo_total_gol ) as totalGolHT,
					( a.eqp_mandante_total_gol + a.eqp_visitante_total_gol ) as totalGolFT,
			
					( a.eqp_mandante_primeito_tempo_total_escanteio + a.eqp_visitante_primeito_tempo_total_escanteio ) as totalEscanteioHT,
					( a.eqp_mandante_total_escanteio + a.eqp_visitante_total_escanteio ) as totalEscanteioFT,
					
					case(a.mandante_result_primeito_tempo_gol)
						when 1 then 'success'
						when 0 then 'warning'
			            else 'danger'
					end resultGolHT,
			        
					case(a.mandante_result_gol)
						when 1 then 'success'
						when 0 then 'warning'
			            else 'danger'
					end resultGolFT,
					
					
					case(a.mandante_result_primeito_tempo_escanteio)
						when 1 then 'success'
						when 0 then 'warning'
			            else 'danger'
					end resultEscanteioHT,
			        
					case(a.mandante_result_escanteio)
						when 1 then 'success'
						when 0 then 'warning'
			            else 'danger'
					end resultEscanteioFT,
					
					CONCAT(a.eqp_mandante_total_gol, ' X ', a.eqp_visitante_total_gol) as resultPlacar,
					CONCAT(a.eqp_mandante_total_escanteio, ' X ', a.eqp_visitante_total_escanteio) as resultPlacarEscanteio
					
			
			FROM jogo as a
					join equipe as visitante on ( a.eqp_visitante_id = visitante.id ) 
			where a.campeonato_id = ?1
					and a.eqp_mandante_id = ?2
			order by a.numero_rodada desc 
			limit ?3
			""", nativeQuery = true)
	List<IJogoDados> findJogosMandante(Long idCampeonato, Long idMandante, Long limiteDeJogos);
	
	
	@Query(value="""
			SELECT 	a.numero_rodada as numeroRodada,
					mandante.nome as equipe,

					( a.eqp_mandante_primeito_tempo_total_gol + a.eqp_visitante_primeito_tempo_total_gol ) as totalGolHT,
					( a.eqp_mandante_total_gol + a.eqp_visitante_total_gol ) as totalGolFT,
			
					( a.eqp_mandante_primeito_tempo_total_escanteio + a.eqp_visitante_primeito_tempo_total_escanteio ) as totalEscanteioHT,
					( a.eqp_mandante_total_escanteio + a.eqp_visitante_total_escanteio ) as totalEscanteioFT,
					
					
					case(a.mandante_result_primeito_tempo_gol)
						when 1 then 'danger'
						when 0 then 'warning'
			            else 'success'
					end resultGolHT,
			        
					case(a.mandante_result_gol)
						when 1 then 'danger'
						when 0 then 'warning'
			            else 'success'
					end resultGolFT,
					
					
					case(a.mandante_result_primeito_tempo_escanteio)
						when 1 then 'danger'
						when 0 then 'warning'
			            else 'success'
					end resultEscanteioHT,
			        
					case(a.mandante_result_escanteio)
						when 1 then 'danger'
						when 0 then 'warning'
			            else 'success'
					end resultEscanteioFT,
					
					CONCAT(a.eqp_mandante_total_gol, ' X ', a.eqp_visitante_total_gol) as resultPlacar,
					CONCAT(a.eqp_mandante_total_escanteio, ' X ', a.eqp_visitante_total_escanteio) as resultPlacarEscanteio
					
			
			FROM jogo as a
					join equipe as mandante on ( a.eqp_mandante_id = mandante.id )
			where a.campeonato_id = ?1
					and a.eqp_visitante_id = ?2
			order by a.numero_rodada desc 
			limit ?3
			""", nativeQuery = true)
	List<IJogoDados> findJogosVisitante(Long idCampeonato, Long idVisitante, Long limiteDeJogos);
	
	@Query(value = """
			select 	a.numero_rodada as numeroRodada,
					count(1) as qtd
				from jogo as a
				where a.campeonato_id = ?1
				group by a.numero_rodada
				order by a.numero_rodada desc;
			""", nativeQuery = true)
	List<IJogoRodadas> FindNumeroRodadas(Long idCampeonato);
	
	
	@Query(value = """
			select 	a.id,
				concat(mandante.nome, ' X ', visitante.nome) as Partida
				from jogo as a
						join equipe as mandante on ( a.eqp_mandante_id = mandante.id )
				        join equipe as visitante on ( a.eqp_visitante_id = visitante.id )
				where a.campeonato_id = ?1
					  and a.numero_rodada = ?2
				order by a.id;
			""", nativeQuery = true)
	List<IJogoPartida> FindPartidaPorRodada(Long idCampeonato , Integer numeroRodada);
	
	
	
	@Query(value = """
			select 	ROW_NUMBER() OVER (ORDER BY result.qtd desc, result.nome) as ordem,
					result.*
			from (
				select 	b.nome,
						count(1) as qtd
				from jogo as a
						join equipe as b on ( a.eqp_mandante_id = b.id )
				where a.campeonato_id = ?1
					  and a.eqp_mandante_total_gol > a.eqp_visitante_total_gol
				 group by b.nome
				 order by qtd desc
				 ) as result;			
			""", nativeQuery = true)
	List<IMelhorEquipe> resultadoMelhorMandante(Long idCampeonato);	
	
	
	@Query(value = """
			
			select 	ROW_NUMBER() OVER (ORDER BY result.qtd desc, result.nome) as ordem,
					result.*
			from (
				select 	b.nome,
						count(1) as qtd
				from jogo as a
						join equipe as b on ( a.eqp_visitante_id = b.id )
				where a.campeonato_id = ?1
					  and a.eqp_visitante_total_gol > a.eqp_mandante_total_gol
				 group by b.nome
				 order by qtd desc
				 ) as result;
	 			
 			""", nativeQuery = true)
	List<IMelhorEquipe> resultadoMelhorVisitante(Long idCampeonato);
	
	
	
	@Query(value = """
			select 	ROW_NUMBER() OVER (ORDER BY result.qtd desc, result.nome) as ordem,
					result.*
			from (
				select 	b.nome,
						count(1) as qtd
				from jogo as a
						join equipe as b on ( a.eqp_mandante_id = b.id )
				where a.campeonato_id = ?1
					  and a.eqp_mandante_total_escanteio > a.eqp_visitante_total_escanteio
				 group by b.nome
				 order by qtd desc
				 ) as result;			
			""", nativeQuery = true)
	List<IMelhorEquipe> resultadoMelhorMandanteEscanteio(Long idCampeonato);	
	
	
	
	@Query(value = """
			
			select 	ROW_NUMBER() OVER (ORDER BY result.qtd desc, result.nome) as ordem,
					result.*
			from (
				select 	b.nome,
						count(1) as qtd
				from jogo as a
						join equipe as b on ( a.eqp_visitante_id = b.id )
				where a.campeonato_id = ?1
					  and a.eqp_visitante_total_escanteio > a.eqp_mandante_total_escanteio
				 group by b.nome
				 order by qtd desc
				 ) as result;
	 			
 			""", nativeQuery = true)
	List<IMelhorEquipe> resultadoMelhorVisitanteEscanteio(Long idCampeonato);
	
		
	
	
	
	
	
	
}