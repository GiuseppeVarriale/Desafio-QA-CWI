package br.com.treinamentoapiproject.tests.ping.tests;

import br.com.treinamentoapiproject.suites.HealthCheck;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.ping.requests.GetPingRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

@Feature("HealthCheck")
public class GetPingTest extends BaseTest {
    GetPingRequest getPingRequest = new GetPingRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Category(HealthCheck.class)
    @DisplayName("Verificar se API está online")
    @Description("Teste consiste em fazer uma requisição para o endpoint Ping da api, verificar se recebe um retorno de status 200" +
            "em até 2 segundos e verificar se o body contém o campo Ok como descrito na documentação")
    public void healthCheckPing() throws Exception {
        getPingRequest.pingApi().then()
                .statusCode(200) //Deveria retornar 200, está retornado 201, alinhar com equipe do projeto/abrir bug
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("$", hasKey("ok")); //Deve retornar o campo OK.

    }
}
