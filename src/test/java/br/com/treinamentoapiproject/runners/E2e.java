package br.com.treinamentoapiproject.runners;


import br.com.treinamentoapiproject.tests.booking.tests.DeleteBookingTest;
import br.com.treinamentoapiproject.tests.booking.tests.GetBookingTest;
import br.com.treinamentoapiproject.tests.booking.tests.PostBookingTest;
import br.com.treinamentoapiproject.tests.booking.tests.PutBookingTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(br.com.treinamentoapiproject.suites.E2e.class)
@Suite.SuiteClasses({
        DeleteBookingTest.class,
        GetBookingTest.class,
        PostBookingTest.class,
        PutBookingTest.class,

})

public class E2e {
}
