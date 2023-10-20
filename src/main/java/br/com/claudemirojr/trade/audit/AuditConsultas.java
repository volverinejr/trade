package br.com.claudemirojr.trade.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.claudemirojr.trade.dto.LogPesquisaDto;
import br.com.claudemirojr.trade.security.Security;

@Aspect
@Component
public class AuditConsultas {
	final static Logger log = LoggerFactory.getLogger(AuditConsultas.class);

	@Value("${spring.application.name}")
	private String servico;

	@Pointcut(value = "execution(* br.com.claudemirojr.*.controller.*.findBy*(..) )")
	public void myPointcut() {
	}

	@Around("myPointcut()")
	public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String className = pjp.getTarget().getClass().toString();
		String methodName = pjp.getSignature().getName();
		Object[] array = pjp.getArgs();
		String argumento = mapper.writeValueAsString(array);

		Object object = pjp.proceed();

		String retorno = mapper.writeValueAsString(object);

		String[] result = className.split("\\.");

		LogPesquisaDto logPesquisaDto = new LogPesquisaDto(this.servico, Security.getUsuarioLogado(), result[result.length - 1], methodName,
				argumento, retorno);

		log.debug(logPesquisaDto.toString());

		return object;
	}

}