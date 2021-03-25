package br.com.treinamentoapiproject.runners;

import br.com.treinamentoapiproject.tests.auth.tests.PostAuthTest;
import br.com.treinamentoapiproject.tests.booking.tests.GetBookingTest;
import br.com.treinamentoapiproject.tests.booking.tests.PutBookingTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(br.com.treinamentoapiproject.suites.Contract.class)
@Suite.SuiteClasses({
        GetBookingTest.class,
        PutBookingTest.class,
        PostAuthTest.class
})
public class Contract {
}
