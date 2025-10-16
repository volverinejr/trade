CREATE TABLE IF NOT EXISTS equipe (
	id bigint NOT NULL AUTO_INCREMENT,
	
	nome varchar(100) NOT NULL,
	
	created_by varchar(255) DEFAULT NULL,
	created_date datetime(6) DEFAULT NULL,
	last_modified_by varchar(255) DEFAULT NULL,
	last_modified_date datetime(6) DEFAULT NULL,
	
	PRIMARY KEY (id),
	UNIQUE KEY UK_p4ksbvsgbo8wj16fwlrhma67l (nome)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS equipe_audit (
	id bigint NOT NULL,
	rev int NOT NULL,
	revtype tinyint DEFAULT NULL,
	
	created_by varchar(255) DEFAULT NULL,
	created_date datetime(6) DEFAULT NULL,
	last_modified_by varchar(255) DEFAULT NULL,
	last_modified_date datetime(6) DEFAULT NULL,
	
	nome varchar(100) DEFAULT NULL,
	PRIMARY KEY (rev,id),
	CONSTRAINT FKev597quytsnrmpddq0t7iwxop FOREIGN KEY (rev) REFERENCES revinfo (rev)
) ENGINE=InnoDB;
