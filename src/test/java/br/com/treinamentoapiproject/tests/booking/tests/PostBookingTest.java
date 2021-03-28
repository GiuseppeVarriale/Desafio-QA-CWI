package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.tests.base.requests.BaseRequest;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.PostBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.CoreMatchers.equalTo;

public class PostBookingTest extends BaseTest {

    PostBookingRequest postBookingRequest = new PostBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Criar uma reserva")
    public void criarReserva(){
        postBookingRequest.criarReserva(Utils.validPayloadBooking())
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .body("booking.firstname", equalTo("Ronaldo"));


    }
}
