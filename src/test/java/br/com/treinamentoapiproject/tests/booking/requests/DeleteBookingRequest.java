package br.com.treinamentoapiproject.tests.booking.requests;

import br.com.treinamentoapiproject.tests.auth.requests.PostAuthRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class DeleteBookingRequest {
    PostAuthRequest postAuthRequest = new PostAuthRequest();


    @Step("Excluir uma reserva com token")
    public Response deleteBookingUsingToken(int id) throws Exception {
        return given()
                .header("Content-type", "application/json")
                .header("Cookie", postAuthRequest.getToken())
                .when()
                .delete("booking/" + id);
    }

    @Step("Excluir uma reserva sem autorização")
    public Response deleteBookingNoAuthenticatoin(int id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete("booking/" + id);
    }
}
