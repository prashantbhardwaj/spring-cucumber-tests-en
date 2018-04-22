package name.alexkosarev.sandbox.web.controllers.contacts;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class BookStepDefinitions extends RestAssuredCucumberRestDocIntegrationTest {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    private String ENDPOINT_GET_BOOK_BY_ISBN = "https://www.googleapis.com/books/v1/volumes";


    @Given("a book exists with an isbn of (.*)")
    public void a_book_exists_with_isbn(String isbn){

        request = given(documentationSpec)
                .param("q", "isbn:" + isbn);
    }

    @When("a user retrieves the book by isbn")
    public void a_user_retrieves_the_book_by_isbn(){
        response = request
                .filter(document("Book-response", documentRelaxedResponseFields()))
                .when()
                .get(ENDPOINT_GET_BOOK_BY_ISBN);
        System.out.println("response: " + response.prettyPrint());
    }

    @Then("the status code is (\\d+)")
    public void verify_status_code(int statusCode){

        json = response
                .then()
                .statusCode(statusCode);
    }

    @And("response includes the following$")
    public void response_equals(Map<String,String> responseFields){
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if(StringUtils.isNumeric(field.getValue())){
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            }
            else{
                json.body(field.getKey(), equalTo(field.getValue()));
            }
        }
    }

    @And("response includes the following in any order")
    public void response_contains_in_any_order(Map<String,String> responseFields){
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if(StringUtils.isNumeric(field.getValue())){
                json.body(field.getKey(), containsInAnyOrder(Integer.parseInt(field.getValue())));
            }
            else{
                json.body(field.getKey(), containsInAnyOrder(field.getValue()));
            }
        }
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    private RequestFieldsSnippet documentRelaxedRequestFields() {
        return relaxedRequestFields();
    }

    private ResponseFieldsSnippet documentRelaxedResponseFields() {
        return relaxedResponseFields();
    }


    private RequestFieldsSnippet documentRequestFields() {
        return requestFields(
                fieldWithPath("id").description("Id of the input that is bigger than 0"),
                fieldWithPath("name").description("Name of the input")
        );
    }

    private ResponseFieldsSnippet documentResponseFields() {
        return responseFields(
                fieldWithPath("kind").description("Indicated whether input message was valid"),
                fieldWithPath("totalItems").description("Some message about the request processing")
        );
    }
}
