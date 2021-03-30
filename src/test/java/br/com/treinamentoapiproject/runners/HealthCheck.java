package br.com.treinamentoapiproject.runners;

import br.com.treinamentoapiproject.tests.ping.tests.GetPingTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(br.com.treinamentoapiproject.suites.HealthCheck.class)
@Suite.SuiteClasses({
        GetPingTest.class
})
public class HealthCheck {

}
