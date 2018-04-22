package name.alexkosarev.sandbox.web.controllers.contacts;

import name.alexkosarev.sandbox.repositories.ContactRepository;
import name.alexkosarev.sandbox.SpringCucumberTestsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockSnippet;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ContextConfiguration(classes = SpringCucumberTestsApplication.class)
public abstract class SpringCucumberIntegrationTest {

    @Autowired
    @MockBean
    ContactRepository contactRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    private ManualRestDocumentation restDocumentation;

    public void setUp() {
        // Spring Restdocs
        restDocumentation = new ManualRestDocumentation("target/generated-snippets");

        // MockMVC
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())// applying Spring Security
                .apply(documentationConfiguration(restDocumentation)// applying Spring Restdocs
                        .snippets().withAdditionalDefaults(new WireMockSnippet()))// applying Spring Cloud Contract и WireMock
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();

        // Spring Restdocs context initialization
        restDocumentation.beforeTest(FindAllTest.class, "setUp");
    }

    public void tearDown() {
        // Spring Restdocs context shutdown
        restDocumentation.afterTest();
    }
}