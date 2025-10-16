package br.com.claudemirojr.trade.util;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiRequestBuilder {

    private String url;
    private Object body;
    private String method = "GET"; // default é GET

    public ApiRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public ApiRequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public ApiRequestBuilder method(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public Response execute() {
        var request = RestAssured.given();

        if (body != null) {
            request.body(body);
        }

        return switch (method) {
            case "POST" -> request.when().post(url);
            case "PUT" -> request.when().put(url);
            case "DELETE" -> request.when().delete(url);
            default -> request.when().get(url);
        };
    }
    
    
    public <T> List<T> executeAndExtractList(String path, Class<T> responseClass, int expectedStatus) {
        return execute().then()
                .statusCode(expectedStatus)
                .extract()
                .jsonPath()
                .getList(path, responseClass);
    }
    
}
