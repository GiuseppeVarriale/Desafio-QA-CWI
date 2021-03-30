package br.com.treinamentoapiproject.tests.auth.tests;

import br.com.treinamentoapiproject.suites.Acceptance;
import br.com.treinamentoapiproject.suites.HealthCheck;
import br.com.treinamentoapiproject.tests.auth.requests.PostAuthRequest;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

@Feature("Autenticação")
public class PostAuthTest extends BaseTest {
    PostAuthRequest postAuthRequest = new PostAuthRequest();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Verificar se retorna o campo token")
    public void validarToken(){
        postAuthRequest.token().then()
                .statusCode(200)
                .time(lessThan(2L), TimeUnit.SECONDS)
                .body("$", hasKey("token"));

    }

}
