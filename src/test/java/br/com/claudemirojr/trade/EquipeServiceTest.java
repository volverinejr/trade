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

import br.com.claudemirojr.trade.model.entity.Equipe;
import br.com.claudemirojr.trade.model.repository.EquipeRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EquipeServiceTest {

    @Autowired
    private EquipeRepository equipeRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        var mysql = MySQLTestContainer.getInstance();
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    void limparBds() {
        equipeRepository.deleteAll();
    }

    @Test
    void inserirEquipe() {
        Equipe equipe = new Equipe();
        equipe.setNome("Tottenham");

        Equipe salvo = equipeRepository.save(equipe);
        List<Equipe> todos = equipeRepository.findAll();
        System.out.println("### Equipes no banco: " + todos);
        assertEquals(1, todos.size());
        assertEquals("Tottenham", todos.get(0).getNome());
    }
}
