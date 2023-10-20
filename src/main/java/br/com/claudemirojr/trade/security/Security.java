package br.com.claudemirojr.trade.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class Security {

	public static String getUsuarioLogado() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}