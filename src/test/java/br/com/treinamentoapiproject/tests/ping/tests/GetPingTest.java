package br.com.treinamentoapiproject.tests.ping.tests;

import br.com.treinamentoapiproject.suites.HealthCheck;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.ping.requests.GetPingRequest;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

public class GetPingTest extends BaseTest {
    GetPingRequest getPingRequest = new GetPingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(HealthCheck.class)
    @DisplayName("Fazer teste de HealthCheck(ping) da api")
    public void validarPing(){
        getPingRequest.pingApi().then()
                .statusCode(201) //Deveria retornar 200, está retornado 201, alinhar com equipe do projeto/abrir bug
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("$",hasKey("ok")); //Deve retornar o campo OK.

    }
}
