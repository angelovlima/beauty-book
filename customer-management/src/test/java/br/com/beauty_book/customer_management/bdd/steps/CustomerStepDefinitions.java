package br.com.beauty_book.customer_management.bdd.steps;

import br.com.beauty_book.customer_management.infra.gateway.jpa.repository.CustomerRepository;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerStepDefinitions {

    private final CustomerRepository repository;

    public CustomerStepDefinitions(CustomerRepository repository) {
        this.repository = repository;
    }

    @LocalServerPort
    private int port;

    private io.restassured.response.Response response;

    private Long registeredCustomerId;

    @Given("no customer exists with CPF {string}")
    public void noCustomerExistsWithCpf(String cpf) {
        repository.findByCpf(cpf).ifPresent(cliente -> repository.deleteById(cliente.getId()));
    }

    @When("I register a customer with the following data:")
    public void iRegisterACustomerWithTheFollowingData(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap();

        String body = formatRequestBody(data);

        RestAssured.port = port;
        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/customers");
    }

    @Then("the customer should be created successfully")
    public void theCustomerShouldBeCreatedSuccessfully() {
        assertThat(response.statusCode())
                .as("Expected HTTP status 201 but got %s with body: %s", response.statusCode(), response.getBody().asString())
                .isEqualTo(201);
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("name")).isNotBlank();
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        assertThat(response.getStatusCode())
                .as("Expected HTTP status %s but got %s with body: %s", expectedStatus, response.statusCode(), response.getBody().asString())
                .isEqualTo(expectedStatus);
    }

    @And("the response message should contain {string}")
    public void theResponseMessageShouldContain(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        assertThat(actualMessage).contains(expectedMessage);
    }

    @And("the response message should be {string}")
    public void theResponseMessageShouldBe(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Given("a customer already exists with CPF {string}")
    public void aCustomerAlreadyExistsWithCpf(String cpf) {
        repository.findByCpf(cpf).ifPresent(cliente -> repository.deleteById(cliente.getId()));

        String body = """
                {
                  "name": "Cliente Existente",
                  "cpf": "%s",
                  "phoneNumber": "1190000-0000",
                  "email": "existente@email.com"
                }
                """.formatted(cpf);

        RestAssured.port = port;
        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/customers");

        assertThat(response.statusCode()).isEqualTo(201);
        registeredCustomerId = response.jsonPath().getLong("id");
    }

    @When("I try to register another customer with the following data:")
    @When("I try to register a customer with the following data:")
    public void iTryToRegisterCustomer(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap();

        RestAssured.port = port;
        response = given()
                .contentType(ContentType.JSON)
                .body(formatRequestBody(data))
                .when()
                .post("/customers");
    }

    @Given("the following customers are already registered:")
    public void theFollowingCustomersAreAlreadyRegistered(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> customers = dataTable.asMaps();

        for (Map<String, String> data : customers) {
            repository.findByCpf(data.get("cpf")).ifPresent(cliente -> repository.deleteById(cliente.getId()));

            response = given()
                    .contentType(ContentType.JSON)
                    .body(formatRequestBody(data))
                    .when()
                    .post("/customers");

            assertThat(response.statusCode()).isEqualTo(201);
        }
    }

    @When("I list all customers")
    public void iListAllCustomers() {
        RestAssured.port = port;
        response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/customers");
    }

    @Then("the response should contain at least {int} customers")
    public void theResponseShouldContainAtLeastCustomers(int expectedMinimum) {
        List<?> customers = response.jsonPath().getList("$");
        assertThat(customers.size()).isGreaterThanOrEqualTo(expectedMinimum);
    }

    @And("the response should contain a customer with name {string} and CPF {string}")
    public void theResponseShouldContainCustomerWithNameAndCpf(String name, String cpf) {
        List<Map<String, Object>> customers = response.jsonPath().getList("$");
        boolean found = customers.stream().anyMatch(customer ->
                name.equals(customer.get("name")) &&
                        cpf.equals(customer.get("cpf"))
        );
        assertThat(found).isTrue();
    }

    @Given("a customer is registered with the following data:")
    public void aCustomerIsRegisteredWithTheFollowingData(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap();

        response = given()
                .contentType(ContentType.JSON)
                .body(formatRequestBody(data))
                .when()
                .post("/customers");

        assertThat(response.statusCode()).isEqualTo(201);
        registeredCustomerId = response.jsonPath().getLong("id");
    }

    @When("I search for the customer by the registered ID")
    public void iSearchForCustomerByRegisteredId() {
        response = given()
                .when()
                .get("/customers/" + registeredCustomerId);
    }

    @When("I search for the customer by ID {long}")
    public void iSearchForCustomerById(Long id) {
        response = given()
                .when()
                .get("/customers/" + id);
    }

    @And("the response should contain the customer name {string}")
    public void theResponseShouldContainCustomerName(String expectedName) {
        String actualName = response.jsonPath().getString("name");
        assertThat(actualName).isEqualTo(expectedName);
    }

    @When("I update the customer by registered ID with the following data:")
    public void iUpdateCustomerByRegisteredId(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap();

        response = given()
                .contentType(ContentType.JSON)
                .body(formatRequestBody(data))
                .when()
                .put("/customers/" + registeredCustomerId);
    }

    @And("the response should contain the name {string} and phoneNumber {string}")
    public void theResponseShouldContainNameAndPhoneNumber(String expectedName, String expectedPhoneNumber) {
        assertThat(response.jsonPath().getString("name")).isEqualTo(expectedName);
        assertThat(response.jsonPath().getString("phoneNumber")).isEqualTo(expectedPhoneNumber);
    }

    @When("I delete the customer by registered ID")
    public void iDeleteCustomerByRegisteredId() {
        response = given()
                .when()
                .delete("/customers/" + registeredCustomerId);
    }

    private String formatRequestBody(Map<String, String> data) {
        return """
                {
                  "name": "%s",
                  "cpf": "%s",
                  "phoneNumber": "%s",
                  "email": "%s"
                }
                """.formatted(
                data.getOrDefault("name", ""),
                data.getOrDefault("cpf", ""),
                data.getOrDefault("phoneNumber", ""),
                data.getOrDefault("email", "")
        );
    }
}