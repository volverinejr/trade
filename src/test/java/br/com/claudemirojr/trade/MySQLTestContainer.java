package br.com.claudemirojr.trade;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MySQLTestContainer {
	
	private static final MySQLContainer<?> CONTAINER;

    static {
        CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34"));
        CONTAINER.start();
    }

    public static synchronized MySQLContainer<?> getInstance() {
        return CONTAINER;
    }

}
