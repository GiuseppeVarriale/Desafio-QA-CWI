package br.com.treinamentoapiproject.tests.booking.requests;

import br.com.treinamentoapiproject.tests.auth.requests.PostAuthRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class PutBookingRequest {

    PostAuthRequest postAuthRequest = new PostAuthRequest();


    @Step("Alterar uma reserva com token")
    public Response updateBookingWithToken(int id, JSONObject payload){
        return given()
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", postAuthRequest.getToken())
                .when()
                .body(payload.toString())
                .put("booking/" + id);
    }


}
