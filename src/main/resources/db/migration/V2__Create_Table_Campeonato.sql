CREATE TABLE IF NOT EXISTS campeonato (
	id bigint NOT NULL AUTO_INCREMENT,

	nome varchar(50) NOT NULL,
	descricao varchar(100) NOT NULL,
	ativo bit(1) NOT NULL,

	created_by varchar(255) DEFAULT NULL,
	created_date datetime(6) DEFAULT NULL,
	last_modified_by varchar(255) DEFAULT NULL,
	last_modified_date datetime(6) DEFAULT NULL,

	PRIMARY KEY (id),
	UNIQUE KEY UK_ndm1apkui2hg5y7ds5m48d8ut (nome)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS campeonato_audit (
	id bigint NOT NULL,
	rev int NOT NULL,
	revtype tinyint DEFAULT NULL,
	ativo bit(1) DEFAULT NULL,
	descricao varchar(100) DEFAULT NULL,
	nome varchar(50) DEFAULT NULL,

	created_by varchar(255) DEFAULT NULL,
	created_date datetime(6) DEFAULT NULL,
	last_modified_by varchar(255) DEFAULT NULL,
	last_modified_date datetime(6) DEFAULT NULL,

	PRIMARY KEY (rev,id),
	CONSTRAINT FKj8rxw7a89prfsoxgj5069l07b FOREIGN KEY (rev) REFERENCES revinfo (rev)
) ENGINE=InnoDB ;
