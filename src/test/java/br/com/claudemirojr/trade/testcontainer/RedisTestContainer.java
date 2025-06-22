package br.com.claudemirojr.trade.testcontainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

public class RedisTestContainer {

	private static final RedisContainer CONTAINERREDIS;

	static {
		CONTAINERREDIS = new RedisContainer(DockerImageName.parse("redis:alpine3.17"));
		CONTAINERREDIS.start();
	}

	public static synchronized RedisContainer getInstance() {
		return CONTAINERREDIS;
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		var redis = RedisTestContainer.getInstance();
		registry.add("spring.redis.host", redis::getHost);
		registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
	}

}
