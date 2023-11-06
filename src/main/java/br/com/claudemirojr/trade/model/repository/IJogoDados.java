package br.com.claudemirojr.trade.model.repository;

public interface IJogoDados {
	Integer getNumeroRodada();
	
	String getEquipe();

	Double getTotalGolHT();

	Double getTotalGolFT();

	Double getTotalEscanteioHT();

	Double getTotalEscanteioFT();

}
