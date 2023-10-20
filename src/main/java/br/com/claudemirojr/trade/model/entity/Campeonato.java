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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campeonato")
@Audited
@AuditTable(value = "campeonato_audit")
@AuditOverride(forClass = EntityBase.class)
@EntityListeners(AuditingEntityListener.class)

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Campeonato extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(length = 50, unique = true, nullable = false)
	private String nome;

	@Column(length = 100, nullable = false)
	private String descricao;

	@Column(nullable = false)
	private Boolean ativo;

	public void Atualizar(String nome, String descricao, Boolean ativo) {
		this.nome = nome;
		this.descricao = descricao;
		this.ativo = ativo;
	}
}