package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.PostBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
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
    GetBookingRequest getBookingRequest = new GetBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Criar uma reserva")
    public void criarReserva() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();

        int id = postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(201)
                .body("booking.firstname", equalTo(payload.get("firstname")))
                .body("booking.lastname", equalTo(payload.get("lastname")))
                .body("booking.depositpaid", equalTo(payload.get("depositpaid")))
                .body("booking.bookingdates.checkin", equalTo(((Map) payload.get("bookingdates")).get("checkin")))
                .body("booking.bookingdates.checkout", equalTo(((Map) payload.get("bookingdates")).get("checkout")))
                .body("booking.additionalneeds", equalTo(payload.get("additionalneeds")))
                .extract().path("bookingid");

        getBookingRequest.getSpecificBooking(id)
                .then()
                .assertThat()
                .statusCode(200)
                .body("firstname", equalTo(payload.get("firstname")));
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Criar 3 reservas seguidas")
    public void createBookingConsecutive() throws Exception {

        for(int i = 0; i <3; i++){
            criarReserva();
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Tentar criar uma reserva com parâmetros a mais no payload")
    public void createBookingWithMorePayloadParameterThanExpected() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();
        payload.put("excessiveParameters", "this is a wrong parameter and should be not accepted");
        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(400);
                //Alinhar com equipe, teste não falhou, é uma característica do software tratar e ignorar parametros a mais?


    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Tentar criar uma reserva com o Header Incorreto")
    public void createBookingWrongAcceptHeader() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();

        postBookingRequest.createBookingWrongAcceptHeader(payload)
                .then()
                .assertThat()
                .statusCode(418);
        //Alinhar com a equipe se realmente era pra ser este código,
        //temos alguma cafeteira fazendo requisição? falando nisso bora buscar um café


    }

}
