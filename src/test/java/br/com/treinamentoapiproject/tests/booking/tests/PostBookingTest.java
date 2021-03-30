package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.suites.E2e;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.PostBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.annotation.Target;
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
    @Category(E2e.class)
    @DisplayName("Criar 3 reservas seguidas")
    public void createBookingConsecutive() throws Exception {

        for(int i = 0; i <3; i++){
            criarReserva();
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Tentar criar uma reserva com dados de tipagem erradas nos campos")
    public void createBookingPayloadFieldsWrongDataType() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();
        JSONObject bookingDates = new JSONObject();
        int inteiro = 123456;
        String numero = "1213123";
        String stringBooleana = "true";


        payload.put("firstname", inteiro);

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500);

        payload = Utils.validPayloadBooking();
        payload.put("lastname", inteiro);

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500);

        payload = Utils.validPayloadBooking();
        payload.put("totalprice", numero);

      postBookingRequest.createBooking(payload)
               .then().assertThat()
               .statusCode(500); //alinhar com equipe, verificar se estão tratanto e atualizar a documentação, ou se abrir bug
                                  // caso estejam tratando no software modificar para 201 e validar se o body está retornando um número no campo


        payload = Utils.validPayloadBooking();
        payload.put("depositpaid", stringBooleana);

        postBookingRequest.createBooking(payload)
                .then().assertThat()
                .statusCode(500); //alinhar com equipe, verificar se estão tratanto e atualizar a documentação, ou se abrir bug
        // caso estejam tratando no software modificar para 201 e validar se o body está retornando um número no campo

        payload = Utils.validPayloadBooking();
        payload.put("additionalneeds", inteiro);

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500);//alinhar com equipe, verificar se estão tratanto e atualizar a documentação, ou se abrir bug
                                 // caso estejam tratando no software modificar para 201 e validar se o body está retornando um string no campo

        payload = Utils.validPayloadBooking();
        bookingDates.put("checkin", inteiro);
        bookingDates.put("checkout", "2027-01-12");
        payload.put("bookingdates", bookingDates);

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500); //abrir bug , está convertendo dados inteiros para data (provavelmente segundos como linux faz)

        bookingDates.put("checkin", "2015-04-12");
        bookingDates.put("checkout", inteiro);
        payload.put("bookingdates", bookingDates);

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500); //abrir bug está , convertendo dados inteiros para data(provavelmente segundos como o linux faz)
    }



    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Tentar criar uma reserva com o Header Accept incorreto")
    public void createBookingWrongAcceptHeader() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();

        postBookingRequest.createBookingWrongAcceptHeader(payload)
                .then()
                .assertThat()
                .statusCode(418);
        //Alinhar com a equipe se realmente era pra ser este código,pelo que vi na internet provavelmente seria 406
        //temos alguma cafeteira fazendo requisição? falando nisso bora buscar um café


    }

}
