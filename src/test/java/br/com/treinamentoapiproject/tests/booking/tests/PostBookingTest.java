package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.PostBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

@Feature("Reservas")
public class PostBookingTest extends BaseTest {

    PostBookingRequest postBookingRequest = new PostBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Criar uma reserva")
    public void criarReserva() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();

        postBookingRequest.createBooking(payload)
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .body("booking.firstname", equalTo(payload.get("firstname")))
                .body("booking.lastname", equalTo(payload.get("lastname")))
                .body("booking.depositpaid", equalTo(payload.get("depositpaid")))
                .body("booking.bookingdates.checkin", equalTo(((Map) payload.get("bookingdates")).get("checkin")))
                .body("booking.bookingdates.checkout", equalTo(((Map) payload.get("bookingdates")).get("checkout")))
                .body("booking.additionalneeds", equalTo(payload.get("additionalneeds")));

    }
}
