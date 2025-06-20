package br.com.claudemirojr.trade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;
import br.com.claudemirojr.trade.testcontainer.MySQLTestContainer;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampeonatoServiceTest extends MySQLTestContainer {

    @Autowired
    private CampeonatoRepository campeonatoRepository;
    

    @BeforeEach
    void limparBds() {
        campeonatoRepository.deleteAll();
    }

    @Test
    void inserirCampeonato() {
    	String nome = "Premier League 2023/24"; 
        Campeonato campeonato = new Campeonato();
        
        campeonato.setNome(nome);
        campeonato.setDescricao("Principal competição de pontos corridos da Inglaterra");
        campeonato.setAtivo(true);
        campeonatoRepository.save(campeonato);
        
        List<Campeonato> campeonatos = campeonatoRepository.findAll();
        
        assertEquals(1, campeonatos.size());
        
        assertEquals(nome, campeonatos.get(0).getNome());
    }
}
