package br.com.treinamentoapiproject.tests.ping.requests;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetPingRequest {


    @Step("Fazer chamada no endpoint de HealthCheck da api(ping).")
    public Response pingApi() {
        return given()
                .get("ping");

    }
}
