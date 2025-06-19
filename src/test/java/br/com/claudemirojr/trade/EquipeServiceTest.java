package br.com.claudemirojr.trade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.claudemirojr.trade.model.entity.Equipe;
import br.com.claudemirojr.trade.model.repository.EquipeRepository;
import br.com.claudemirojr.trade.testcontainer.MySQLTestContainer;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EquipeServiceTest extends MySQLTestContainer {

	@Autowired
	private EquipeRepository equipeRepository;

	@BeforeEach
	void limparBds() {
		equipeRepository.deleteAll();
	}

	@Test
	void inserirEquipe() {
		Equipe equipe = new Equipe();
		equipe.setNome("Tottenham");

		equipeRepository.save(equipe);
		List<Equipe> todos = equipeRepository.findAll();
		System.out.println("### Equipes no banco: " + todos);
		assertEquals(1, todos.size());
		assertEquals("Tottenham", todos.get(0).getNome());
	}
}
