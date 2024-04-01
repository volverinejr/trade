package br.com.claudemirojr.trade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Home", description = "Verificar a saúde da aplicação")
@RestController
@RequestMapping("${url.base}/home/v1")
public class HomeController {

	@GetMapping("/up")
	public ResponseEntity<?> getUp() {
		return ResponseEntity.ok("UP");
	}

}
