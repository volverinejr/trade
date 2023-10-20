package br.com.claudemirojr.trade.model.entity;

import java.io.Serializable;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import br.com.claudemirojr.trade.model.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jogo")
@Audited
@AuditTable(value = "jogo_audit")
@AuditOverride(forClass = EntityBase.class)
@EntityListeners(AuditingEntityListener.class)

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Jogo extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Integer GREEN = 1;
	public static final Integer EMPATE = 0;
	public static final Integer RED = -1;

	@OneToOne(fetch = FetchType.EAGER)
	@NotNull
	private Campeonato campeonato;

	@Column(nullable = false)
	private Integer numeroRodada;

	// ===== MANDANTE =====
	@OneToOne(fetch = FetchType.EAGER)
	@NotNull
	private Equipe eqpMandante;

	@Column(nullable = false)
	private Integer eqpMandantePrimeitoTempoTotalGol;

	@Column(nullable = false)
	private Integer eqpMandantePrimeitoTempoTotalEscanteio;

	@Column(nullable = false)
	private Integer eqpMandanteSegundoTempoTotalGol;

	@Column(nullable = false)
	private Integer eqpMandanteSegundoTempoTotalEscanteio;

	@Column(nullable = false)
	private Integer eqpMandanteTotalGol;

	@Column(nullable = false)
	private Integer eqpMandanteTotalEscanteio;

	// ===== VISITANTE =====
	@OneToOne(fetch = FetchType.EAGER)
	@NotNull
	private Equipe eqpVisitante;

	@Column(nullable = false)
	private Integer eqpVisitantePrimeitoTempoTotalGol;

	@Column(nullable = false)
	private Integer eqpVisitantePrimeitoTempoTotalEscanteio;

	@Column(nullable = false)
	private Integer eqpVisitanteSegundoTempoTotalGol;

	@Column(nullable = false)
	private Integer eqpVisitanteSegundoTempoTotalEscanteio;

	@Column(nullable = false)
	private Integer eqpVisitanteTotalGol;

	@Column(nullable = false)
	private Integer eqpVisitanteTotalEscanteio;

	// ===== RESULTADO =====
	@Column(nullable = false)
	private Integer mandanteResultPrimeitoTempoGol;

	@Column(nullable = false)
	private Integer mandanteResultPrimeitoTempoEscanteio;

	@Column(nullable = false)
	private Integer mandanteResultSegundoTempoGol;

	@Column(nullable = false)
	private Integer mandanteResultSegundoTempoEscanteio;

	@Column(nullable = false)
	private Integer mandanteResultGol;

	@Column(nullable = false)
	private Integer mandanteResultEscanteio;

	public Jogo(@NotNull Campeonato campeonato, Integer numeroRodada,

			@NotNull Equipe eqpMandante, Integer eqpMandantePrimeitoTempoTotalGol,
			Integer eqpMandantePrimeitoTempoTotalEscanteio, Integer eqpMandanteSegundoTempoTotalGol,
			Integer eqpMandanteSegundoTempoTotalEscanteio,

			@NotNull Equipe eqpVisitante, Integer eqpVisitantePrimeitoTempoTotalGol,
			Integer eqpVisitantePrimeitoTempoTotalEscanteio, Integer eqpVisitanteSegundoTempoTotalGol,
			Integer eqpVisitanteSegundoTempoTotalEscanteio) {
		super();

		this.campeonato = campeonato;
		this.numeroRodada = numeroRodada;

		this.eqpMandante = eqpMandante;
		this.eqpMandantePrimeitoTempoTotalGol = eqpMandantePrimeitoTempoTotalGol;
		this.eqpMandantePrimeitoTempoTotalEscanteio = eqpMandantePrimeitoTempoTotalEscanteio;
		this.eqpMandanteSegundoTempoTotalGol = eqpMandanteSegundoTempoTotalGol;
		this.eqpMandanteSegundoTempoTotalEscanteio = eqpMandanteSegundoTempoTotalEscanteio;

		this.eqpVisitante = eqpVisitante;
		this.eqpVisitantePrimeitoTempoTotalGol = eqpVisitantePrimeitoTempoTotalGol;
		this.eqpVisitantePrimeitoTempoTotalEscanteio = eqpVisitantePrimeitoTempoTotalEscanteio;
		this.eqpVisitanteSegundoTempoTotalGol = eqpVisitanteSegundoTempoTotalGol;
		this.eqpVisitanteSegundoTempoTotalEscanteio = eqpVisitanteSegundoTempoTotalEscanteio;
	}

	public void Atualizar(Campeonato campeonato, Integer numeroRodada,

			Equipe eqpMandante, Integer eqpMandantePrimeitoTempoTotalGol,
			Integer eqpMandantePrimeitoTempoTotalEscanteio, Integer eqpMandanteSegundoTempoTotalGol,
			Integer eqpMandanteSegundoTempoTotalEscanteio,

			Equipe eqpVisitante, Integer eqpVisitantePrimeitoTempoTotalGol,
			Integer eqpVisitantePrimeitoTempoTotalEscanteio, Integer eqpVisitanteSegundoTempoTotalGol,
			Integer eqpVisitanteSegundoTempoTotalEscanteio) {
		this.campeonato = campeonato;
		this.numeroRodada = numeroRodada;
		this.eqpMandante = eqpMandante;
		this.eqpMandantePrimeitoTempoTotalGol = eqpMandantePrimeitoTempoTotalGol;
		this.eqpMandantePrimeitoTempoTotalEscanteio = eqpMandantePrimeitoTempoTotalEscanteio;
		this.eqpMandanteSegundoTempoTotalGol = eqpMandanteSegundoTempoTotalGol;
		this.eqpMandanteSegundoTempoTotalEscanteio = eqpMandanteSegundoTempoTotalEscanteio;

		this.eqpVisitante = eqpVisitante;
		this.eqpVisitantePrimeitoTempoTotalGol = eqpVisitantePrimeitoTempoTotalGol;
		this.eqpVisitantePrimeitoTempoTotalEscanteio = eqpVisitantePrimeitoTempoTotalEscanteio;
		this.eqpVisitanteSegundoTempoTotalGol = eqpVisitanteSegundoTempoTotalGol;
		this.eqpVisitanteSegundoTempoTotalEscanteio = eqpVisitanteSegundoTempoTotalEscanteio;
	}

	@PrePersist
	@PreUpdate
	public void FazerAnalise() {
		// ===== MANDANTE =====
		this.eqpMandanteTotalGol = this.eqpMandantePrimeitoTempoTotalGol + this.eqpMandanteSegundoTempoTotalGol;
		this.eqpMandanteTotalEscanteio = this.eqpMandantePrimeitoTempoTotalEscanteio
				+ this.eqpMandanteSegundoTempoTotalEscanteio;

		// ===== VISITANTE =====
		this.eqpVisitanteTotalGol = this.eqpVisitantePrimeitoTempoTotalGol + this.eqpVisitanteSegundoTempoTotalGol;
		this.eqpVisitanteTotalEscanteio = this.eqpVisitantePrimeitoTempoTotalEscanteio
				+ this.eqpVisitanteSegundoTempoTotalEscanteio;

		// ===== RESULTADO =====
		var valorAux = this.eqpMandantePrimeitoTempoTotalGol - this.eqpVisitantePrimeitoTempoTotalGol;
		if (valorAux > 0) {
			this.mandanteResultPrimeitoTempoGol = GREEN;
		} else if (valorAux < 0) {
			this.mandanteResultPrimeitoTempoGol = RED;
		} else {
			this.mandanteResultPrimeitoTempoGol = EMPATE;
		}

		
		valorAux = this.eqpMandantePrimeitoTempoTotalEscanteio - this.eqpVisitantePrimeitoTempoTotalEscanteio;
		if (valorAux > 0) {
			this.mandanteResultPrimeitoTempoEscanteio = GREEN;
		} else if (valorAux < 0) {
			this.mandanteResultPrimeitoTempoEscanteio = RED;
		} else {
			this.mandanteResultPrimeitoTempoEscanteio = EMPATE;
		}

		
		valorAux = this.eqpMandanteSegundoTempoTotalGol - this.eqpVisitanteSegundoTempoTotalGol;
		if (valorAux > 0) {
			this.mandanteResultSegundoTempoGol = GREEN;
		} else if (valorAux < 0) {
			this.mandanteResultSegundoTempoGol = RED;
		} else {
			this.mandanteResultSegundoTempoGol = EMPATE;
		}

		
		valorAux = this.eqpMandanteSegundoTempoTotalEscanteio - this.eqpVisitanteSegundoTempoTotalEscanteio;
		if (valorAux > 0) {
			this.mandanteResultSegundoTempoEscanteio = GREEN;
		} else if (valorAux < 0) {
			this.mandanteResultSegundoTempoEscanteio = RED;
		} else {
			this.mandanteResultSegundoTempoEscanteio = EMPATE;
		}

		
		valorAux = this.eqpMandanteTotalGol - this.eqpVisitanteTotalGol;
		if (valorAux > 0) {
			this.mandanteResultGol = GREEN;
		} else if (valorAux < 0) {
			this.mandanteResultGol = RED;
		} else {
			this.mandanteResultGol = EMPATE;
		}

		
		valorAux = this.eqpMandanteTotalEscanteio - this.eqpVisitanteTotalEscanteio;
		if (valorAux > 0) {
			this.mandanteResultEscanteio = GREEN;
		} else if (valorAux < 0) {
			this.mandanteResultEscanteio = RED;
		} else {
			this.mandanteResultEscanteio = EMPATE;
		}

	}

}