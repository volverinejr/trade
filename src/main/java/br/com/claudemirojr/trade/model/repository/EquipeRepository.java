package br.com.claudemirojr.trade.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.claudemirojr.trade.model.entity.Equipe;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

	Page<Equipe> findByIdGreaterThanEqual(Long id, Pageable pageable);

	Page<Equipe> findByNomeIgnoreCaseContaining(String nome, Pageable pageable);

	List<Equipe> findAllByOrderByNomeAsc();
}