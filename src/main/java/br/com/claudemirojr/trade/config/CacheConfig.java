package br.com.claudemirojr.trade.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class CacheConfig {
	
	private static final Duration DEFAULT_TTL = Duration.ofMinutes(60);
	private static final Duration SHORT_TTL = Duration.ofMinutes(3);
	private static final Duration MEDIUM_TTL = Duration.ofMinutes(5);	

	@Bean
	RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		return (builder) -> builder
				.withCacheConfiguration("trade_campeonatoCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(SHORT_TTL))

				.withCacheConfiguration("trade_equipeCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(SHORT_TTL))

				.withCacheConfiguration("trade_jogoCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(SHORT_TTL))

				.withCacheConfiguration("trade_jogoAnaliseMandanteVisitanteCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(SHORT_TTL))

				.withCacheConfiguration("customerCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(MEDIUM_TTL));
	}

	@Bean
	RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(DEFAULT_TTL).disableCachingNullValues()
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

}