package br.com.claudemirojr.trade.util;

import br.com.claudemirojr.trade.dto.LoginRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AuthUtil {
	
	private static String cachedToken;
	
    public static String obterToken() {
        if (cachedToken != null) {
            return cachedToken;
        }

        RestAssured.useRelaxedHTTPSValidation();

        String username = System.getenv("TEST_USERNAME");
        String password = System.getenv("TEST_PASSWORD");
        String redis = System.getenv("REDIS_HOST");
        

        if (username == null || password == null || redis == null ) {
            throw new IllegalStateException("Variáveis de ambiente TEST_USERNAME, TEST_PASSWORD e REDIS_HOST devem estar definidas.");
        }

        String url = "https://grapp.tec.br/newapi/token";

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        Response response = RestAssured.given()
            .contentType("application/json")
            .body(loginRequest)
            .when()
            .post(url)
            .then()
            .statusCode(200)
            .extract()
            .response();

        cachedToken = response.jsonPath().getString("accessToken");

        return cachedToken;
    }	

}
