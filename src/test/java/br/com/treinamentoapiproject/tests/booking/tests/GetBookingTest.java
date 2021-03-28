package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.suites.Contract;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

@Feature("Reservas")
public class GetBookingTest extends BaseTest {
    GetBookingRequest getBookingRequest = new GetBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Lista IDs das Reservas")
    public void validarIdsDasRerservas() throws Exception{
        getBookingRequest.allBookings().then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()",greaterThan(0));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar uma reserva específica")
    public void validateSpecificBooking() throws Exception {

        getBookingRequest.getSpecificBooking(getBookingRequest.getAnExistingBookingId())
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()",greaterThan(0));
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Category(Contract.class)
    @DisplayName("Garantir o contrato do retorno de reserva específica")
    public void garantirContratoListaReservaEspecífica() throws Exception {
        getBookingRequest.getSpecificBooking(getBookingRequest.getAnExistingBookingId()).then()
                .statusCode(200)
                .assertThat()
                .body(
                        matchesJsonSchema(
                                new File(
                                        Utils.getContractsBasePath("booking", "booking")
                                        // Campo additionalneeds está retornando apenas quando tem info no db,
                                        // verificar com equipe se este campo deveria ser documentado como opcional na
                                        // e criação de reserva, possível bug na api
                                )
                        )
                );
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Category(Contract.class)
    @DisplayName("Garantir o contrato do retorno da lista de reservas")
    public void garantirContratoListaReserva() throws Exception{
        getBookingRequest.allBookings().then()
                .statusCode(200)
                .assertThat()
                .body(
                        matchesJsonSchema(
                            new File(
                                    Utils.getContractsBasePath("booking","bookings")
                            )
                        )
                );
    }



}
