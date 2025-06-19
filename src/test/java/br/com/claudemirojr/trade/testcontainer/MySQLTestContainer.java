package br.com.claudemirojr.trade.testcontainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MySQLTestContainer {
	
	private static final MySQLContainer<?> CONTAINER;

    static {
        CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.29"));
        CONTAINER.start();
    }

    public static synchronized MySQLContainer<?> getInstance() {
        return CONTAINER;
    }
    
	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		var mysql = MySQLTestContainer.getInstance();
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}    
    


}
