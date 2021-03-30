package br.com.treinamentoapiproject.tests.booking.requests;

import br.com.treinamentoapiproject.tests.auth.requests.PostAuthRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class PutBookingRequest {

    PostAuthRequest postAuthRequest = new PostAuthRequest();


    @Step("Alterar uma reserva com token")
    public Response updateBookingWithToken(int id, JSONObject payload) {
        return given()
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", postAuthRequest.getToken())
                .when()
                .body(payload.toString())
                .put("booking/" + id);
    }


    @Step("Alterar uma reserva com basic authorization")
    public Response updateBookingUsingBasicAuth(int id, JSONObject payload) {
        return given()
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", Utils.basicAuthorizationEncode("admin", "password123"))
                .when()
                .body(payload.toString())
                .put("booking/" + id);
    }

    @Step("Alterar uma reserva com token incorreto")
    public Response updateBookingWithWrongToken(int id, JSONObject payload) {
        return given()
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "abc12341231")
                .when()
                .body(payload.toString())
                .put("booking/" + id);
    }

    @Step("Alterar uma reserva sem token")
    public Response updateBookingWithoutToken(int id, JSONObject payload) {
        return given()
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .when()
                .body(payload.toString())
                .put("booking/" + id);
    }

}
