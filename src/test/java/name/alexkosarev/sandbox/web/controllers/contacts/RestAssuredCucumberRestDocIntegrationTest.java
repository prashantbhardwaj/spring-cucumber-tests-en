package name.alexkosarev.sandbox.web.controllers.contacts;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import name.alexkosarev.sandbox.SpringCucumberTestsApplication;
import name.alexkosarev.sandbox.repositories.ContactRepository;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockSnippet;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SpringCucumberTestsApplication.class)
public abstract class RestAssuredCucumberRestDocIntegrationTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    // Spring Restdocs
    @Rule
    public final ManualRestDocumentation restDocumentation = new ManualRestDocumentation("target/generated-snippets");

    protected RequestSpecification documentationSpec;

    public void setUp() {

        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();

        // Spring Restdocs context initialization
        restDocumentation.beforeTest(RestAssuredCucumberRestDocIntegrationTest.class, "setUp");
    }

}