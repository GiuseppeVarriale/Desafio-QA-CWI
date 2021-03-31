package br.com.treinamentoapiproject.runners;

import br.com.treinamentoapiproject.tests.auth.requests.PostAuthRequest;
import br.com.treinamentoapiproject.tests.auth.tests.PostAuthTest;
import br.com.treinamentoapiproject.tests.base.tests.BaseTest;
import br.com.treinamentoapiproject.tests.booking.requests.GetBookingRequest;
import br.com.treinamentoapiproject.tests.booking.tests.DeleteBookingTest;
import br.com.treinamentoapiproject.tests.booking.tests.GetBookingTest;
import br.com.treinamentoapiproject.tests.booking.tests.PostBookingTest;
import br.com.treinamentoapiproject.tests.booking.tests.PutBookingTest;
import br.com.treinamentoapiproject.tests.ping.tests.GetPingTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(br.com.treinamentoapiproject.suites.AllTests.class)
@Suite.SuiteClasses({
        BaseTest.class,
        GetPingTest.class,
        PostAuthTest.class,
        GetBookingTest.class,
        PostBookingTest.class,
        PutBookingTest.class,
        DeleteBookingTest.class
})
public class AllTests {
}
