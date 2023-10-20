ALTER TABLE jogo 
ADD UNIQUE INDEX UNK_jogo_ja_cadastrado (campeonato_id ASC, numero_rodada ASC, eqp_mandante_id ASC, eqp_visitante_id ASC);
