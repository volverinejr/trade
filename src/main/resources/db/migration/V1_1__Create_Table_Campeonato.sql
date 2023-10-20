CREATE TABLE `campeonato` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  `ativo` bit(1) NOT NULL,
  
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ndm1apkui2hg5y7ds5m48d8ut` (`nome`)
  
) ENGINE=InnoDB;