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
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@Feature("Reservas")
public class GetBookingTest extends BaseTest {
    GetBookingRequest getBookingRequest = new GetBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Lista IDs das Reservas")
    public void listBookingIds() throws Exception {
        getBookingRequest.allBookings().then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar uma reserva específica")
    public void listSpecificBooking() throws Exception {

        getBookingRequest.getSpecificBooking(getBookingRequest.getAnExistingBookingId())
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro firstname")
    public void listsIdFilteredByFirstname() throws Exception {

        Map<String, String> existingBookingDataMap = getBookingRequest.getExitingDataMapForFilter();
        Map<String, String> filtersMap = new HashMap<>();
        String firstname = existingBookingDataMap.get("firstname");
        filtersMap.put("firstname", firstname);

        List<Integer> ids = getBookingRequest.getFilteredBookingIdList(filtersMap)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .extract()
                .jsonPath().getList("bookingid");

        Random rand = new Random();

        int samples = 0;
        if (ids.size() <= 2 && ids.size() > 0) {
            samples = ids.size();
        } else {
            samples = 2;
        }

        for (int i = 0; i < samples; i++) {
            int id = ids.get(rand.nextInt(ids.size()));
            getBookingRequest.getSpecificBooking(id)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("firstname", equalTo(firstname));
        }


    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro lastname")
    public void listsIdFilteredByLastname() throws Exception {

        Map<String, String> existingBookingDataMap = getBookingRequest.getExitingDataMapForFilter();
        Map<String, String> filtersMap = new HashMap<>();
        String lastname = existingBookingDataMap.get("lastname");
        filtersMap.put("lastname", lastname);

        List<Integer> ids = getBookingRequest.getFilteredBookingIdList(filtersMap)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .extract()
                .jsonPath().getList("bookingid");

        Random rand = new Random();

        int samples = 0;
        if (ids.size() <= 2 && ids.size() > 0) {
            samples = ids.size();
        } else {
            samples = 2;
        }

        for (int i = 0; i < samples; i++) {
            int id = ids.get(rand.nextInt(ids.size()));
            getBookingRequest.getSpecificBooking(id)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("lastname", equalTo(lastname));
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro checkin")
    public void listsIdFilteredBycheckin() throws Exception {

        Map<String, String> existingBookingDataMap = getBookingRequest.getExitingDataMapForFilter();
        Map<String, String> filtersMap = new HashMap<>();
        String existingBookingId = existingBookingDataMap.get("bookingid");
        String checkinToFilter = existingBookingDataMap.get("checkin");
        filtersMap.put("checkin", checkinToFilter);

        List<Integer> ids = getBookingRequest.getFilteredBookingIdList(filtersMap)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .extract().jsonPath().getList("bookingid");

        Random rand = new Random();

        int samples = 0;

        if (ids.size() <= 2 && ids.size() > 0) {
            samples = ids.size();
        } else {
            samples = 2;
        }

        for (int i = 0; i < samples; i++) {
            int id = ids.get(rand.nextInt(ids.size()));
            String filterResultCheckin = getBookingRequest.getSpecificBooking(id)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .extract().path("bookingdates.checkin");
            //valida se a data das amostras é  maior ou igual a data enviada no filtro
            assertTrue(Utils.dateIsAfterOrEqualThan(filterResultCheckin, checkinToFilter));
        }
        //valida se a id do dado usado para retirar a data enviada no filtro está presente,
        // validando que está também retornando data de checkin igual a enviada no filtro
        assertTrue(ids.contains(existingBookingId));
        //verificar com a equipe, não está retornando no filtro booking com data igual a informada para filtrar
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro checkout")
    public void listsIdFilteredBycheckout() throws Exception {

        Map<String, String> existingBookingDataMap = getBookingRequest.getExitingDataMapForFilter();
        Map<String, String> filtersMap = new HashMap<>();
        String checkoutToFilter = existingBookingDataMap.get("checkout");
        String existingBookingId = existingBookingDataMap.get("bookingid");
        filtersMap.put("checkout", checkoutToFilter);

        List<Integer> ids = getBookingRequest.getFilteredBookingIdList(filtersMap)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .extract().jsonPath().getList("bookingid");

        Random rand = new Random();

        int samples = 0;
        if (ids.size() <= 2 && ids.size() > 0) {
            samples = ids.size();
        } else {
            samples = 2;
        }

        for (int i = 0; i < samples; i++) {
            int id = ids.get(rand.nextInt(ids.size()));
            String filterResultCheckout = getBookingRequest.getSpecificBooking(id)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .extract().path("bookingdates.checkout");

            //valida se a data das amostras é  menor ou igual a data enviada no filtro

            assertTrue(Utils.dateIsBeforeOrEqualThan(filterResultCheckout, checkoutToFilter)); //alinhar com equipe
            //provavelmente documentação incorreta, não faz muito sentido eu filtrar maior que a data, seria reduntante
            // filtro checkin...

            //valida se a id do dado usado para retirar a data enviada no filtro está presente,
            // validando que está também retornando data de checkin igual a enviada no filtro
            assertTrue(ids.contains(existingBookingId));
            //verificar com a equipe, não está retornando no filtro booking com data igual a informada para filtrar
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro name, checkin e checkout ")
    public void listsIdFilteredByNameCheckinCheckout() throws Exception {

        Map<String, String> existingBookingDataMap = getBookingRequest.getExitingDataMapForFilter();
        Map<String, String> filtersMap = new HashMap<>();
        String existingBookingId = existingBookingDataMap.get("bookingid");


        String nameToFilter = existingBookingDataMap.get("firstname");
        String checkinToFilter = existingBookingDataMap.get("checkin");
        String checkoutToFilter = existingBookingDataMap.get("checkout");

        filtersMap.put("name", nameToFilter);
        //Alinhar com equipe, este campos não existe na documentação da api e está retornando todos ids

        filtersMap.put("checkin", checkinToFilter);
        filtersMap.put("checkout", checkoutToFilter);


        List<Integer> ids = getBookingRequest.getFilteredBookingIdList(filtersMap)
                .then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("size()", greaterThan(0))
                .extract().jsonPath().getList("bookingid");

        Random rand = new Random();

        int samples = 0;
        if (ids.size() <= 2 && ids.size() > 0) {
            samples = ids.size();
        } else {
            samples = 2;
        }

        for (int i = 0; i < samples; i++) {
            int id = ids.get(rand.nextInt(ids.size()));
            ValidatableResponse result = getBookingRequest.getSpecificBooking(id)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("size()", greaterThan(0));


            String filterResultCheckin = result.extract().path("bookingdates.checkin");
            String filterResultCheckout = result.extract().path("bookingdates.checkout");

            String filterResultFirstname = result.extract().path("firstname");
            String filterResultlastname = result.extract().path("lastname");

            //valida se alguma das amostras contém o requisitado no filtro name em alguma parte do nome
            assertTrue(filterResultFirstname.contains(nameToFilter)
                    || filterResultlastname.contains(nameToFilter));

            //valida se a data das amostras é  maior ou igual a data enviada no filtro
            assertTrue(Utils.dateIsAfterOrEqualThan(filterResultCheckin, checkinToFilter));

            //valida se a data das amostras é  menor ou igual a data enviada no filtro
            assertTrue(Utils.dateIsBeforeOrEqualThan(filterResultCheckout, checkoutToFilter)); //alinhar com equipe
            //provavelmente documentação incorreta, não faz muito sentido eu filtrar maior que a data, seria reduntante
            // filtro checkin...

            //valida se a id do dado usado para retirar a data enviada no filtro está presente,
            // validando que está também retornando data de checkin igual a enviada no filtro
            assertTrue(ids.contains(existingBookingId));
            //verificar com a equipe, não está retornando no filtro booking com data igual a informada para filtrar
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Exibir erro na tentativa de filtrar com filtro mal formatado")
    public void listIdsBadFilterFormatTest() throws Exception {

        Map<String, String> filtersMap = new HashMap<>();
        filtersMap.put("checkin", "20201102");


        getBookingRequest.getFilteredBookingIdList(filtersMap)
                .then()
                .statusCode(500)
                .time(lessThan(2L), TimeUnit.SECONDS);

        filtersMap.clear();
        filtersMap.put("checkout", "20201102");

        getBookingRequest.getFilteredBookingIdList(filtersMap)
                .then()
                .statusCode(500)
                .time(lessThan(2L), TimeUnit.SECONDS);

        filtersMap.put("checkin", "20-2011-02");

        getBookingRequest.getFilteredBookingIdList(filtersMap)
                .then()
                .statusCode(500)
                .time(lessThan(2L), TimeUnit.SECONDS);

    }


    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Category(Contract.class)
    @DisplayName("Garantir o contrato do retorno de reserva específica")
    public void specificBookingContractTest() throws Exception {
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
    public void BookingsIdsListContractTest() throws Exception {
        getBookingRequest.allBookings().then()
                .statusCode(200)
                .assertThat()
                .body(
                        matchesJsonSchema(
                                new File(
                                        Utils.getContractsBasePath("booking", "bookings")
                                )
                        )
                );
    }


}
