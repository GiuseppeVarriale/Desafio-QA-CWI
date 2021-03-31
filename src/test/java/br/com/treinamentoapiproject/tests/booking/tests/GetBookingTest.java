package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.suites.Contract;
import br.com.treinamentoapiproject.suites.E2e;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@Feature("Reservas")
public class GetBookingTest extends BaseTest {
    GetBookingRequest getBookingRequest = new GetBookingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Lista IDs das Reservas")
    @Description("Teste consiste em fazer uma requisição para o endpoint booking, verificar se retorna status 200 e" +
            "se o conteúdo do body é maior que 0")
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
    @Description("Teste consiste em fazer uma requisição para o endpoint booking listando as ids existentes, e a " +
            "partir daí faz uma requisição enviando uma Id válida, verifica se retorna status 200 e se o conteúdo" +
            " do body é maior que 0")
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
    @Description("Teste consiste em fazer uma requisição de uma lista de dados existente em um registro de reserva" +
            " aleatório e gravar num map,fazer uma requisição de uma lista de ids filtrando pelo parâmetro firstname " +
            "do map, verificar se retorna código 200, se o body é maior que 0, gravar os ids desta lista, se tiver " +
            "retornado 2 ids ou mais, pegar 2 delas, se não o número que retornou, verificar se encontra ela na " +
            "requisição pelo id, verificar se o código retornado é 200, validar se o firstname delas é igual ou " +
            "contém o usado no filtro e validar se o registro original também esta incluso na lista")
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
            System.out.println(firstname);
            getBookingRequest.getSpecificBooking(id)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("firstname", containsString(firstname));
        }


    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro lastname")
    @Description("Teste consiste em fazer uma requisição de uma lista de dados existente em um registro de reserva " +
            "aleatório e gravar num map, fazer uma requisição de uma lista de ids filtrando pelo parâmetro lastname " +
            "do map, verificar se retorna código 200, se o body é maior que 0, gravar os ids desta lista, se tiver " +
            "retornado 2 ids ou mais, pegar 2 delas, se não o número que retornou, requisitar a amostra pelo id," +
            "verificar se o código retornado é 200, validar se o lastname delas é igual ou contém o usado no filtro " +
            "e validar se o registro original foi retornado na lista")
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
                    .body("lastname", containsString(lastname));
        }
        //valida se a id do registro usado para gerar o nome enviado no filtro está presente,
        assertTrue(ids.contains(existingBookingDataMap.get("bookingid")));

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro checkin")
    @Description("Teste consiste em fazer uma requisição de uma lista de dados existente em um registro de reserva " +
            "aleatório e gravar num map,fazer uma requisição de uma lista de ids filtrando pelo parâmetro checkin do " +
            "map, verificar se retorna código 200, se o body é maior que 0, gravar os ids desta lista, se tiver " +
            "retornado 2 ids ou mais, pegar 2 delas, se não o número que retornou, requisitar a amostra pelo id," +
            "verificar se o código retornado é 200, validar se o checkin delas é maior ou igual usado no filtro " +
            "e validar se o registro original retornou na lista, garantindo que data de checkin igual a do filtro " +
            "também retornou")
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
    @Description("Teste consiste em fazer uma requisição de uma lista de dados existente em um registro de reserva " +
            "aleatório e gravar num map, fazer uma requisição de uma lista de ids filtrando pelo parâmetro " +
            "checkout do map, verificar se retorna código 200, se o body é maior que 0,gravar os ids desta lista, se " +
            "tiver retornado 2 ids ou mais, pegar 2 delas, se não o número que retornou, requisitar a amostra pelo id, " +
            "verificar se o código retornado é 200, validar se o checkout delas é menor ou igual usado no filtro e" +
            " validar se o registro original retornou na lista, garantindo que data checkin igual a do filltro" +
            " também retornou")
    public void listsIdFilteredByCheckout() throws Exception {

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
        }

        //valida se a id do dado usado para retirar a data enviada no filtro está presente,
        // validando que está também retornando data de checkin igual a enviada no filtro
        assertTrue(ids.contains(existingBookingId));
        //verificar com a equipe, não está retornando no filtro booking com data igual a informada para filtrar
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar IDs de reservas utilizando o filtro checkout duplamente colocado na url, e retornar status 500")
    @Description("Teste consiste em fazer uma requisição de uma lista de dados existente em um registro de reserva " +
            "aleatório e gravar num map, fazer uma requisição de uma lista de ids filtrando pelo parâmetro checkout " +
            "do map, porém colocado repetido como parâmetro e validar que o servidor retorna Status 500")
    public void listsIdFilteredByCheckoutCheckout() throws Exception {

        Map<String, String> existingBookingDataMap = getBookingRequest.getExitingDataMapForFilter();
        String checkoutToFilter = existingBookingDataMap.get("checkout");

        getBookingRequest.getFilteredByCheckoutCheckoutBookingIdList(checkoutToFilter)
                .then()
                .statusCode(500);
        //Alinhar com equipe, se era pra ter algum tratamento de campos repetidos na api
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro name, checkin e checkout ")
    @Description("Teste consiste em fazer uma requisição de uma lista de dados existente em um registro de reserva " +
            "aleatório e gravar num map, fazer uma requisição de uma lista de ids filtrando pelo parâmetro name(usando " +
            "o valor firstname do map para preencher o campo), checking e checkout do map verificar se retorna " +
            "código 200, se o body é maior que 0, gravar os ids desta lista, se tiver retornado 2 ids ou mais, " +
            "pegar 2 delas, se não o número que retornou, requisitar a amostra pelo id, verificar se o código" +
            " retornado é 200, validar se o checking delas é maior ou igual usado no filtro, validar se o checkout " +
            "delas é menor ou igual usado no filtro, validar se o firstname ou lastname contém o valor usado no " +
            "parametro name e validar se o registro original retornou na lista, garantido que datas de checkin e " +
            "checkout iguais aos requisitados no filtro também retornaram")
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
        }

        //valida se a id do dado usado para retirar a data enviada no filtro está presente,
        // validando que está também retornando data de checkin igual a enviada no filtro
        assertTrue(ids.contains(existingBookingId));
        //verificar com a equipe, não está retornando no filtro booking com data igual a informada para filtrar
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(Acceptance.class)
    @DisplayName("Listar Id reservas usando filtro firstname, checkin e checkout ")
    @Description("Teste consiste em fazer uma requisição de uma lista de dados existente em um registro de reserva " +
            "aleatório e gravar num map, fazer uma requisição de uma lista de ids filtrando peloz parâmetroz firstname," +
            " checking e checkout do map, verificar se retorna código 200, se o body é maior que 0, gravar os ids desta" +
            " lista, se tiver retornado 2 ids ou mais,pegar 2 delas, se não o número que retornou, requisitar a amostra" +
            " pelo id, verificar se o código retornado é 200, validar se o checking delas é maior ou igual usado no" +
            " filtro, validar se o checkout delas é menor ou igual usado no filtro, validar se o firstname do body " +
            "contém o valor usado no parametro firstname do filtro e validar se o registro original retornou na lista," +
            " garantido que datas de checkin e checkout iguais aos requisitados no filtro também retornaram")
    public void listsIdFilteredByFirstnameCheckinCheckout() throws Exception {

        Map<String, String> existingBookingDataMap = getBookingRequest.getExitingDataMapForFilter();
        Map<String, String> filtersMap = new HashMap<>();
        String existingBookingId = existingBookingDataMap.get("bookingid");


        String nameToFilter = existingBookingDataMap.get("firstname");
        String checkinToFilter = existingBookingDataMap.get("checkin");
        String checkoutToFilter = existingBookingDataMap.get("checkout");

        filtersMap.put("firstname", nameToFilter);
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

            //valida se alguma das amostras contém o requisitado no filtro firstname
            assertTrue(filterResultFirstname.contains(nameToFilter));

            //valida se a data das amostras é  maior ou igual a data enviada no filtro
            assertTrue(Utils.dateIsAfterOrEqualThan(filterResultCheckin, checkinToFilter));

            //valida se a data das amostras é  menor ou igual a data enviada no filtro
            assertTrue(Utils.dateIsBeforeOrEqualThan(filterResultCheckout, checkoutToFilter)); //alinhar com equipe
            //provavelmente documentação incorreta, não faz muito sentido eu filtrar maior que a data, seria reduntante
            // filtro checkin...
        }

        //valida se a id do dado usado para retirar a data enviada no filtro está presente,
        // validando que está também retornando data de checkin igual a enviada no filtro
        assertTrue(ids.contains(existingBookingId));
        //verificar com a equipe, não está retornando no filtro booking com data igual a informada para filtrar
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Visualizar erro de servidor 500 quando enviar filtro mal formatado")
    @Description("Teste consiste em fazer requisições com o filtros mal formatados, como firstname e lastname " +
            "são campos strings e o filtro é feito na url, não teriam formatos para serem testado," +
            "então o teste faz requisições com formatos incorretos de datas para filtrar no checkin e no checkoutm e " +
            "espera código 500 em até 2 segundos")
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
    @DisplayName("Garantir o contrato do retorno de um reserva específica")
    @Description("Teste consiste em fazer uma requisição de uma reserva existente pela id, verificar se o" +
            "status retornado é 200 e se o body respeita o contrato")
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
    @Description("Teste consiste em fazer uma requisição de uma lista de ids existentes, verificar se o" +
            "status retornado é 200 e se o body respeita o contrato")
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
