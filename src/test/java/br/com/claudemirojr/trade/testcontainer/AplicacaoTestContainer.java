package br.com.claudemirojr.trade.testcontainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

public class AplicacaoTestContainer {

	private static final MySQLContainer<?> CONTAINERMYSQL;
	private static final RedisContainer CONTAINERREDIS;

	
	static {
		CONTAINERMYSQL = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.29"));
		CONTAINERMYSQL.start();
		
		CONTAINERREDIS = new RedisContainer(DockerImageName.parse("redis:alpine3.17"));
		CONTAINERREDIS.start();
	}

	public static synchronized MySQLContainer<?> getInstanceMysql() {
		return CONTAINERMYSQL;
	}

	public static synchronized RedisContainer getInstanceRedis() {
		return CONTAINERREDIS;
	}

	
	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		var mysql = AplicacaoTestContainer.getInstanceMysql();
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		
		var redis = AplicacaoTestContainer.getInstanceRedis();
		registry.add("spring.data.redis.host", redis::getHost);
		registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
		
	}
	
	
	


/*
	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		var redis = RedisTestContainer.getInstance();
		registry.add("spring.redis.host", redis::getHost);
		registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
	}
	*/

}
