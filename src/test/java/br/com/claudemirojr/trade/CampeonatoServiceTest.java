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
        Campeonato campeonato = new Campeonato();
        campeonato.setNome("Premier League 2023/24");
        campeonato.setDescricao("Principal competição de pontos corridos da Inglaterra");
        campeonato.setAtivo(true);

        campeonatoRepository.save(campeonato);
        List<Campeonato> todos = campeonatoRepository.findAll();
        System.out.println("### Campeonatos no banco: " + todos);
        assertEquals(1, todos.size());
        assertEquals("Premier League 2023/24", todos.get(0).getNome());
    }
}
