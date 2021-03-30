package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.DeleteBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.PostBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

public class DeleteBookingTest extends BaseTest {
        DeleteBookingRequest deleteBookingRequest = new DeleteBookingRequest();
        PostBookingRequest postBookingRequest = new PostBookingRequest();
        GetBookingRequest getbookingRequest = new GetBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Excluir uma reserva utilizando token")
    public void deleteBookingUsingToken()  throws Exception{

        int createdBookingId = postBookingRequest.createBooking(Utils.validPayloadBooking())
                .then().statusCode(201)
                .extract().path("bookingid");

        deleteBookingRequest.deleteBookingUsingToken(createdBookingId)
                .then()
                .statusCode(200)
                .body("$", hasKey("ok")); // verificar com equipe , código que estpa sendo retornado é 201, alinhar

        getbookingRequest.getSpecificBooking(createdBookingId)
                .then()
                .statusCode(404);

    }


}
