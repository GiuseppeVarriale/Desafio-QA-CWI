package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.tests.auth.requests.PostAuthRequest;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.DeleteBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.PostBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class DeleteBookingTest extends BaseTest {
        DeleteBookingRequest deleteBookingRequest = new DeleteBookingRequest();
        PostBookingRequest postBookingRequest = new PostBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Excluir uma reserva utilizando token")
    public void validarExcluirUmaReservaUtilizandoToken()  throws Exception{

        int idReservaCriada = postBookingRequest.criarReserva(Utils.validPayloadBooking())
                .then().statusCode(201)
                .extract().path("bookingid");

        deleteBookingRequest.excluirReservaComToken(idReservaCriada)
                .then()
                .statusCode(200);

    }


}
