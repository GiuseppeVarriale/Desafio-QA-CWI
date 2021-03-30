package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.PutBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

@Feature("Reservas")
public class PutBookingTest extends BaseTest {

    PutBookingRequest putBookingRequest = new PutBookingRequest();
    GetBookingRequest getBookingRequest = new GetBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Alterar uma reserva utilizando token")
    public void updateCurrentBookingUsingToken()  throws Exception{

        JSONObject payload = Utils.validPayloadBooking();
        int id = getBookingRequest.getAnExistingBookingId();


        putBookingRequest.updateBookingWithToken(id, payload)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()",greaterThan(0))
                .body("firstname", equalTo(payload.get("firstname")))
                .body("lastname", equalTo(payload.get("lastname")))
                .body("depositpaid", equalTo(payload.get("depositpaid")))
                .body("bookingdates.checkin", equalTo(((Map)payload.get("bookingdates")).get("checkin")))
                .body("bookingdates.checkout", equalTo(((Map)payload.get("bookingdates")).get("checkout")))
                .body("additionalneeds", equalTo(payload.get("additionalneeds")));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Alterar uma reserva utilizando basic authorization")
    public void updateCurrentBookingUsingBasicAuth()  throws Exception{

        JSONObject payload = Utils.validPayloadBooking();
        int id = getBookingRequest.getAnExistingBookingId();

        putBookingRequest.updateBookingUsingBasicAuth(id, payload)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()",greaterThan(0))
                .body("firstname", equalTo(payload.get("firstname")))
                .body("lastname", equalTo(payload.get("lastname")))
                .body("depositpaid", equalTo(payload.get("depositpaid")))
                .body("bookingdates.checkin", equalTo(((Map)payload.get("bookingdates")).get("checkin")))
                .body("bookingdates.checkout", equalTo(((Map)payload.get("bookingdates")).get("checkout")))
                .body("additionalneeds", equalTo(payload.get("additionalneeds")));
    }


}
