package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.suites.E2e;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.PutBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    @Description("Teste consiste em buscar uma reserva existente, tentar alterar a mesma usando um Token válido, com" +
            " um payload completo, verificar se o código é 201 é retornado, e se os dados do body batem com os " +
            " no payload")
    public void updateBookingUsingToken() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();
        int id = getBookingRequest.getAnExistingBookingId();


        putBookingRequest.updateBookingWithToken(id, payload)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .body("firstname", equalTo(payload.get("firstname")))
                .body("lastname", equalTo(payload.get("lastname")))
                .body("depositpaid", equalTo(payload.get("depositpaid")))
                .body("bookingdates.checkin", equalTo(((Map) payload.get("bookingdates")).get("checkin")))
                .body("bookingdates.checkout", equalTo(((Map) payload.get("bookingdates")).get("checkout")))
                .body("additionalneeds", equalTo(payload.get("additionalneeds")));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Alterar uma reserva utilizando Basic Auth")
    @Description("Teste consiste em buscar uma reserva existente, tentar alterar a mesma usando basic Auth, com um " +
            "payload completo, verificar se o código é 201 é retornado, e se os dados do body batem com os enviados " +
            "no payload")
    public void updateBookingUsingBasicAuth() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();
        int id = getBookingRequest.getAnExistingBookingId();

        putBookingRequest.updateBookingUsingBasicAuth(id, payload)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .body("firstname", equalTo(payload.get("firstname")))
                .body("lastname", equalTo(payload.get("lastname")))
                .body("depositpaid", equalTo(payload.get("depositpaid")))
                .body("bookingdates.checkin", equalTo(((Map) payload.get("bookingdates")).get("checkin")))
                .body("bookingdates.checkout", equalTo(((Map) payload.get("bookingdates")).get("checkout")))
                .body("additionalneeds", equalTo(payload.get("additionalneeds")));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Tentar alterar uma reserva quando o token não for enviado e receber status 401")
    @Description("Teste consiste em buscar uma reserva existente, tentar alterar a mesma sem autenticação, " +
            " com um payload completo e receber um retorno 401 " )
    public void updateBookingWithoutToken() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();
        int id = getBookingRequest.getAnExistingBookingId();

        putBookingRequest.updateBookingWithoutToken(id, payload)
                .then()
                .statusCode(401);


    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Tentar alterar uma reserva quando o token enviado for inválido e receber status 401")
    @Description("Teste consiste em buscar uma reserva existente, tentar alterar a mesma com token inválido,com um " +
            "payload completo e receber um retorno 401" )
    public void updateBookingUsingWrongToken() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();
        int id = getBookingRequest.getAnExistingBookingId();

        putBookingRequest.updateBookingWithWrongToken(id, payload)
                .then()
                .statusCode(401);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Tentar alterar uma reserva que não existe")
    @Description("Teste consiste em buscar uma lista de reservas existentes, pegar o valor mais alto da lista, somar " +
            "um valor para ficar fora do range de ids exisentes, tentar alterar uma reserva com esse id e receber o " +
            "código 404" )
    public void updateAnInexistentBooking() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();
        List<Integer> ids = getBookingRequest.allBookings()
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract()
                .jsonPath().getList("bookingid");

        int id = Collections.max(ids) + 10; //Soma 10 a id com a maior número da lista

        putBookingRequest.updateBookingWithToken(id, payload)
                .then()
                .statusCode(404);
        //alinhar com equipe está recebendo código 405
    }

}
