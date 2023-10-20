package br.com.claudemirojr.trade.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.claudemirojr.trade.model.entity.Campeonato;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Long> {

	Page<Campeonato> findByIdGreaterThanEqual(Long id, Pageable pageable);

	Page<Campeonato> findByNomeIgnoreCaseContaining(String nome, Pageable pageable);

	List<Campeonato> findByAtivo(Boolean ativo);
}