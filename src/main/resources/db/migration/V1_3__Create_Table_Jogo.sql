CREATE TABLE jogo (
	id bigint NOT NULL AUTO_INCREMENT,
	
	campeonato_id bigint NOT NULL,
	numero_rodada int NOT NULL,
	
	eqp_mandante_id bigint NOT NULL,
	eqp_mandante_primeito_tempo_total_gol int NOT NULL,
	eqp_mandante_primeito_tempo_total_escanteio int NOT NULL,
	eqp_mandante_segundo_tempo_total_gol int NOT NULL,
	eqp_mandante_segundo_tempo_total_escanteio int NOT NULL,
	eqp_mandante_total_gol int NOT NULL,
	eqp_mandante_total_escanteio int NOT NULL,
	
	eqp_visitante_id bigint NOT NULL,
	eqp_visitante_primeito_tempo_total_gol int NOT NULL,
	eqp_visitante_primeito_tempo_total_escanteio int NOT NULL,
	eqp_visitante_segundo_tempo_total_gol int NOT NULL,
	eqp_visitante_segundo_tempo_total_escanteio int NOT NULL,
	eqp_visitante_total_gol int NOT NULL,
	eqp_visitante_total_escanteio int NOT NULL,
	
	mandante_result_primeito_tempo_gol int NOT NULL,
	mandante_result_primeito_tempo_escanteio int NOT NULL,
	mandante_result_segundo_tempo_gol int NOT NULL,
	mandante_result_segundo_tempo_escanteio int NOT NULL,
	mandante_result_gol int NOT NULL,
	mandante_result_escanteio int NOT NULL,
	
	
	created_by varchar(255),
	created_date datetime(6),
	
	last_modified_by varchar(255),
	last_modified_date datetime(6),
	
	PRIMARY KEY (id),

	CONSTRAINT FK6g9urf6y2yosejdkeie3u56k1 FOREIGN KEY (campeonato_id) REFERENCES campeonato (id),
	CONSTRAINT FK9s7aa5agoc27nfxuc3un3ke2t FOREIGN KEY (eqp_mandante_id) REFERENCES equipe (id),
	CONSTRAINT FKpjg2mihkilhowpc00cvwodgff FOREIGN KEY (eqp_visitante_id) REFERENCES equipe (id)
	
) ENGINE=InnoDB;
