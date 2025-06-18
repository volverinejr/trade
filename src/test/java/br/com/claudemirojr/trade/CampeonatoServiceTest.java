package br.com.claudemirojr.trade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import br.com.claudemirojr.trade.model.entity.Campeonato;
import br.com.claudemirojr.trade.model.repository.CampeonatoRepository;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampeonatoServiceTest {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        var mysql = MySQLTestContainer.getInstance();
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

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

        Campeonato salvo = campeonatoRepository.save(campeonato);
        List<Campeonato> todos = campeonatoRepository.findAll();
        System.out.println("### Campeonatos no banco: " + todos);
        assertEquals(1, todos.size());
        assertEquals("Premier League 2023/24", todos.get(0).getNome());
    }
}
