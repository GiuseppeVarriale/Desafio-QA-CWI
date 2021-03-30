package br.com.treinamentoapiproject.tests.booking.requests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class PostBookingRequest {

    @Step("Criar uma reserva")
    public Response createBooking(JSONObject payload){
        return given()
                .header("Content-type", "application/json")
                .when()
                .body(payload.toString())
                .post("booking");
    }


}
