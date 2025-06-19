package br.com.claudemirojr.trade.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

	@Bean
	AuditorAware<String> auditorProvider() {
		// TODO precisa voltar essa linha e autenticar nos testes
		return () -> Optional.ofNullable( SecurityContextHolder.getContext().getAuthentication().getName());
		
		//return () -> Optional.ofNullable("test-user");
	}
}
