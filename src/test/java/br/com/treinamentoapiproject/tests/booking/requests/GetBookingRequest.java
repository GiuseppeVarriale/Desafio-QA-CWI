package br.com.treinamentoapiproject.tests.booking.requests;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;


public class GetBookingRequest {

    @Step("Buscar todas as reservas.")
    public Response allBookings(){
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get("booking");

    }
    @Step("Retornar uma id existente")
    public int getAnExistingBookingId() {
        List<Integer> ids = this.allBookings()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList("bookingid");
        Random rand = new Random();
        return ids.get(rand.nextInt(ids.size()));
    }

    @Step("Buscar reservas Específica.")
    public Response getSpecificBooking(int id){
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get("booking/" + id);

    }
}
