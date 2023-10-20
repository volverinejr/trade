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
@Table(name = "equipe")
@Audited
@AuditTable(value = "equipe_audit")
@AuditOverride(forClass = EntityBase.class)
@EntityListeners(AuditingEntityListener.class)

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Equipe extends EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(length = 100, unique = true, nullable = false)
	private String nome;

	public void Atualizar(String nome) {
		this.nome = nome;
	}

}