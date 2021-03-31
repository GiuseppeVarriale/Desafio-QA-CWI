package br.com.treinamentoapiproject.tests.booking.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.suites.E2e;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.requests.PostBookingRequest;
import br.com.treinamentoapiproject.utils.Utils;
import io.qameta.allure.Description;
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
    @Description("Teste consiste em criar uma nova reserva usando um payload válido e completo, receber retorno 201, " +
            "validar os dados do body com os enviados no payload, extrair a id retornada, buscar a id nos registros," +
            "validar se ela existe e o firstname é o mesmo que foi enviado no payload para garantir que está criado" +
            "no banco e não foi só retornado no body")
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
    @Category(E2e.class)
    @DisplayName("Criar uma reserva enviando mais parâmetros no payload da reserva e receber código 400")
    @Description("Teste consiste em gerar um payload válido e completo, adicionar um parâmentro excessivo" +
            " tentar criar uma reserva usando esté payload e validar que receberá o código 400")
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
    @Description("Teste consiste em criar 3 reservas consecutivas usando payloads válidos e completos, em cada um deles" +
            " receber retorno 201, validar os dados do body com os enviados no payload, extrair a id retornada, buscar" +
            " a id nos registros, validar se ela existe e o firstname é o mesmo que foi enviado no payload para garantir" +
            " que está criado no banco e não foi só retornado no body")
    public void createBookingConsecutive() throws Exception {

        for (int i = 0; i < 3; i++) {
            criarReserva();
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Validar retorno 500 quando o payload da reserva estiver inválido(tipagem de dados)")
    @Description("Teste consiste em testar a criação de reservas com campos de tipagem errada, por exemplo" +
            "campo firstname espera uma string e enviar inteiro, campo totalprice espera um numero e recebe uma string" +
            "e assim por diante, esse teste testa todos os campos com dados de tipagem errada, e espera que o servidor" +
            "retorne o código 500")
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
    @DisplayName("Validar retorno 418 quando o header Accept for inválido")
    @Description("Teste consiste em tentar criar uma reserva enviando o campo Accept inválido, e valida se o servidor" +
            "irá retornar código 408")
    public void createBookingWrongAcceptHeader() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();

        postBookingRequest.createBookingWrongAcceptHeader(payload)
                .then()
                .assertThat()
                .statusCode(418);
        //Alinhar com a equipe se realmente era pra ser este código,pelo que vi na internet provavelmente seria 406
        //temos alguma cafeteira fazendo requisição? falando nisso bora buscar um café
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(E2e.class)
    @DisplayName("Validar retorno 500 quando o payload tiver atributos faltando(chave e valor)")
    @Description("Teste consiste em testar a criação de reservas um atributo faltando(chave : valor), por exemplo" +
            "payload faltando a chave first name, e o valor do mesmo, espera-se que o servidor retorne o código 500")
            //alinhar com equipe, foi usado código 500 levando em referência testes de payload anteriores, porém talvez
            //seria mais correto código 400, já que o erro não é do servidor
    public void createBookingPayloadKeysValuesMissing() throws Exception {

        JSONObject payload = Utils.validPayloadBooking();
        JSONObject bookingDates = new JSONObject();

        payload.remove("firstname");

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500);

        payload = Utils.validPayloadBooking();
        payload.remove("lastname");

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500);

        payload = Utils.validPayloadBooking();
        payload.remove("totalprice");

        postBookingRequest.createBooking(payload)
                .then().assertThat()
                .statusCode(500);

        payload = Utils.validPayloadBooking();
        payload.remove("depositpaid");

        postBookingRequest.createBooking(payload)
                .then().assertThat()
                .statusCode(500);

        payload = Utils.validPayloadBooking();
        payload.remove("additionalneeds");

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500);//alinhar com equipe, verificar se estão tratando como opcional, definir se modificar
                                //software ou documentações

        payload = Utils.validPayloadBooking();
        payload.remove("bookingdates");
        bookingDates.put("checkout", "2027-01-12");
        payload.put("bookingdates", bookingDates);

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500);

        payload.remove("bookingdates");
        bookingDates.put("checkin", "2015-04-12");
        bookingDates.remove("checkout");
        payload.put("bookingdates", bookingDates);

        postBookingRequest.createBooking(payload)
                .then()
                .assertThat()
                .statusCode(500);
    }

}
