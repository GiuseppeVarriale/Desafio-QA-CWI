package br.com.treinamentoapiproject.tests.auth.tests;

import br.com.treinamentoapiproject.tests.auth.requests.PostAuthRequest;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThan;

@Feature("Autenticação")
public class PostAuthTest extends BaseTest {
    PostAuthRequest postAuthRequest = new PostAuthRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Verificar se retorna o campo token")
    public void validarToken() {
        postAuthRequest.token().then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("$", hasKey("token"));

    }

}
