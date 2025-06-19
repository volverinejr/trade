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

	@Bean
	RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		return (builder) -> builder
				.withCacheConfiguration("trade_campeonatoCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3)))

				.withCacheConfiguration("trade_equipeCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3)))

				.withCacheConfiguration("trade_jogoCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3)))

				.withCacheConfiguration("trade_jogoAnaliseMandanteVisitanteCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3)))

				.withCacheConfiguration("customerCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
	}

	@Bean
	RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(60)).disableCachingNullValues()
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

}