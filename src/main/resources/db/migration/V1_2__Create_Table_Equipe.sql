CREATE TABLE equipe (
	id bigint NOT NULL AUTO_INCREMENT,
	nome varchar(100) NOT NULL,
	
	created_by varchar(255),
	created_date datetime(6),
	
	last_modified_by varchar(255),
	last_modified_date datetime(6),
	
	PRIMARY KEY (id),
	UNIQUE KEY UK_p4ksbvsgbo8wj16fwlrhma67l (nome)
) ENGINE=InnoDB;