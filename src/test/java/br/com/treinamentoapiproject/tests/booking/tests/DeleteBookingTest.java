package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.suites.E2e;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.DeleteBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.PostBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

@Feature("Reservas")
public class DeleteBookingTest extends BaseTest {
    DeleteBookingRequest deleteBookingRequest = new DeleteBookingRequest();
    PostBookingRequest postBookingRequest = new PostBookingRequest();
    GetBookingRequest getbookingRequest = new GetBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Excluir um reserva com sucesso utilizando token")
    @Description("Teste consiste em logar, criar uma reserva, avaliar se foi criada com sucesso 201, apagar a mesma," +
            " verificar se o codigo foi 200, procurar a mesma, verificar se o retorno foi 404")
    public void deleteBookingUsingToken() throws Exception {

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

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Category(E2e.class)
    @DisplayName("Tentar excluir uma reserva sem autorização e não conseguir")
    @Description("Teste consistem e criar um registro, verificar se retornou 201, tentar apagar sem enviar autenticação," +
            "receber código 401, verificar se o registro criado ainda existe e receber o código 200,")
    public void deleteBookingNoAuthentication() throws Exception {

        int createdBookingId = postBookingRequest.createBooking(Utils.validPayloadBooking())
                .then().statusCode(201)
                .extract().path("bookingid");

        deleteBookingRequest.deleteBookingNoAuthenticatoin(createdBookingId)
                .then()
                .statusCode(401);

        getbookingRequest.getSpecificBooking(createdBookingId)
                .then()
                .statusCode(200);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Tentar excluir um reserva que não existe e exibir erro 404")
    @Description("Teste consistem em fazer uma busca de ids existente, pegar o valor mais alto e tentar excluir um registro" +
            "acima deste range, receber código 404")
    public void deleteInexistentBooking() throws Exception {

        List<Integer> ids = getbookingRequest.allBookings()
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .extract()
                .jsonPath().getList("bookingid");

        int id = Collections.max(ids) + 10; //Soma 10 a id com a maior numero da lista

        deleteBookingRequest.deleteBookingUsingToken(id)
                .then()
                .statusCode(404);
        //Alinhar com equipe, está retornando 405, abrir bug

    }

}
