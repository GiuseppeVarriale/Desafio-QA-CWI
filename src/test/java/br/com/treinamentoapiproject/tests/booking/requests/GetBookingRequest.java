package br.com.treinamentoapiproject.tests.booking.requests;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;


public class GetBookingRequest {

    @Step("Buscar todas as reservas.")
    public Response allBookings() {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get("booking");

    }

    @Step("Buscar dados válidos para usar no teste de filtro ")
    public Map<String, String> getExitingDataMapForFilter() throws Exception {
        int bookingId = this.getAnExistingBookingId();
        Map<String, ?> bookingMap = this.getSpecificBooking(bookingId)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .contentType(ContentType.JSON).extract().jsonPath().getJsonObject("");


        String firstname = bookingMap.get("firstname").toString();
        String lastname = bookingMap.get("lastname").toString();
        String checkin = ((Map) bookingMap.get("bookingdates")).get("checkin").toString();
        String checkout = ((Map) bookingMap.get("bookingdates")).get("checkin").toString();
        Map<String, String> existinBookingDataMap = new HashMap<String, String>();
        existinBookingDataMap.put("firstname", firstname);
        existinBookingDataMap.put("lastname", lastname);
        existinBookingDataMap.put("checkin", checkin);
        existinBookingDataMap.put("checkout", checkout);
        existinBookingDataMap.put("bookingid", String.valueOf(bookingId));
        return existinBookingDataMap;


    }


    @Step("Buscar reservas com filtros.")
    public Response getFilteredBookingIdList(Map<String, String> filtersMap) {
        String path = "booking?";
        path = path + filtersMap.entrySet().stream()
                .map((filter) -> filter.getKey() + "=" + filter.getValue()).collect(Collectors.joining("&"));

        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(path);

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
    public Response getSpecificBooking(int id) {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get("booking/" + id);

    }
}
