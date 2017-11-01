package name.alexkosarev.sandbox.web.controllers.contacts;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        glue = "name.alexkosarev.sandbox.web.controllers.contacts",
        features = "classpath:features/contacts/FindAll.feature")
public class FindAllTest {
}
