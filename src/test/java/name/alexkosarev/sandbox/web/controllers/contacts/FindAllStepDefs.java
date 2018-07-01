package name.alexkosarev.sandbox.web.controllers.contacts;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import name.alexkosarev.sandbox.entities.Contact;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindAllStepDefs extends SpringCucumberIntegrationTest {

    private ResultActions resultActions;

    @Before
    public void setUp(Scenario scenario) {
        super.setUp(getScenarioName(scenario));
    }

    @Given("^contacts stored in the storage:$")
    public void contacts_stored_in_the_storage(List<Contact> contacts) throws Throwable {
        doReturn(contacts).when(contactRepository)
                .findAll();
    }

    @When("^client sends GET \"([^\"]*)\"$")
    public void client_sends_GET(String requestPath) throws Throwable {
        resultActions = mockMvc.perform(get(requestPath).with(user("tester")));
    }

    @Then("^service should return response with status (\\d+)$")
    public void service_should_return_response_with_status(int status) throws Throwable {
        resultActions.andExpect(status().is(status));
    }

    @Then("^response body should be compatible with \"([^\"]*)\"$")
    public void response_body_should_be_compatible_with(String contentType) throws Throwable {
        resultActions.andExpect(content().contentTypeCompatibleWith(contentType));
    }

    @Then("^response body should be an array and contain (\\d+) entries$")
    public void response_body_should_be_an_array_and_contain_entries(int entriesCount) throws Throwable {
        resultActions.andExpect(jsonPath("$.length()").value(3));
    }

    @Then("^request and response should be logged$")
    public void request_and_response_should_be_logged() throws Throwable {
        resultActions.andDo(print());
    }

    @Then("^request and response should be documented$")
    public void request_and_response_should_be_documented() throws Throwable {
        resultActions.andDo(document("contacts/findAll",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("[0].email").description("Contact email"),
                        fieldWithPath("[0].name").description("Contact name")
                )
        ));
    }

    @After
    public void tearDown() {
        super.tearDown();
    }
}