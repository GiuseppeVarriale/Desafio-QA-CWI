package br.com.treinamentoapiproject.runners;

import br.com.treinamentoapiproject.tests.booking.tests.GetBookingTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(br.com.treinamentoapiproject.suites.Contract.class)
@Suite.SuiteClasses({
        GetBookingTest.class,
})
public class Contract {
}
