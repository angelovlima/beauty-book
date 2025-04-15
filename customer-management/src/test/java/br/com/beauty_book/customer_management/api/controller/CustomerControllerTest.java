package br.com.beauty_book.customer_management.api.controller;

import br.com.beauty_book.customer_management.api.dto.CustomerApiResponse;
import br.com.beauty_book.customer_management.infra.gateway.jpa.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CustomerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository repository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        repository.deleteAll();
    }

    @Test
    @DisplayName("should create customer successfully")
    void shouldCreateCustomerSuccessfully() {
        String requestBody = """
                {
                  "name": "Carlos Oliveira",
                  "cpf": "123.456.789-00",
                  "phoneNumber": "1199999-9999",
                  "email": "carlos.oliveira@example.com"
                }
                """;

        CustomerApiResponse response =
                given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("/customers")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(CustomerApiResponse.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Carlos Oliveira");
        assertThat(response.cpf()).isEqualTo("123.456.789-00");
        assertThat(response.phoneNumber()).isEqualTo("1199999-9999");
        assertThat(response.email()).isEqualTo("carlos.oliveira@example.com");

        var db = repository.findById(response.id());
        assertThat(db).isPresent();
    }

    @Test
    @DisplayName("should return 409 if customer with CPF already exists")
    void shouldReturnConflictIfCpfAlreadyExists() {
        String existingCpf = "123.456.789-00";

        String firstRequest = """
            {
              "name": "Carlos Oliveira",
              "cpf": "%s",
              "phoneNumber": "1199999-9999",
              "email": "carlos.oliveira@example.com"
            }
            """.formatted(existingCpf);

        String secondRequest = """
            {
              "name": "Joana Souza",
              "cpf": "%s",
              "phoneNumber": "1198888-8888",
              "email": "joana@example.com"
            }
            """.formatted(existingCpf);

        given()
                .contentType(ContentType.JSON)
                .body(firstRequest)
                .when()
                .post("/customers")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(secondRequest)
                .when()
                .post("/customers")
                .then()
                .statusCode(409)
                .body("message",
                        org.hamcrest.Matchers.equalTo("A customer with CPF %s already exists.".formatted(existingCpf)));
    }

    @Test
    @DisplayName("should return 400 if CPF is invalid")
    void shouldReturnBadRequestIfCpfIsInvalid() {
        String request = """
            {
              "name": "Marina Silva",
              "cpf": "12345678900",
              "phoneNumber": "1199999-0000",
              "email": "marina@example.com"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/customers")
                .then()
                .statusCode(400)
                .body("message", org.hamcrest.Matchers.containsString("Invalid CPF"));
    }

    @Test
    @DisplayName("should return 400 if email is invalid")
    void shouldReturnBadRequestIfEmailIsInvalid() {
        String request = """
            {
              "name": "André Souza",
              "cpf": "321.654.987-00",
              "phoneNumber": "1199888-7777",
              "email": "email-invalido"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/customers")
                .then()
                .statusCode(400)
                .body("message", org.hamcrest.Matchers.containsString("Invalid email format"));
    }

    @Test
    @DisplayName("should return list of all customers")
    void shouldReturnListOfCustomers() {
        String request1 = """
            {
              "name": "Carlos Martins",
              "cpf": "111.222.333-44",
              "phoneNumber": "1199999-1111",
              "email": "carlos.martins@example.com"
            }
            """;

        String request2 = """
            {
              "name": "Juliana Reis",
              "cpf": "555.666.777-88",
              "phoneNumber": "1198888-2222",
              "email": "juliana.reis@example.com"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(request1)
                .when()
                .post("/customers")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(request2)
                .when()
                .post("/customers")
                .then()
                .statusCode(201);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/customers")
                .then()
                .statusCode(200)
                .body("size()", org.hamcrest.Matchers.is(2))
                .body("[0].name", org.hamcrest.Matchers.is("Carlos Martins"))
                .body("[1].name", org.hamcrest.Matchers.is("Juliana Reis"));
    }

    @Test
    @DisplayName("should return customer by ID")
    void shouldReturnCustomerById() {
        String request = """
            {
              "name": "Patrícia Lima",
              "cpf": "999.888.777-66",
              "phoneNumber": "1197777-7777",
              "email": "patricia.lima@example.com"
            }
            """;

        int id =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/customers")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/customers/" + id)
                .then()
                .statusCode(200)
                .body("name", org.hamcrest.Matchers.is("Patrícia Lima"))
                .body("cpf", org.hamcrest.Matchers.is("999.888.777-66"));
    }

    @Test
    @DisplayName("should return 404 if customer is not found by ID")
    void shouldReturnNotFoundWhenCustomerDoesNotExist() {
        long nonExistentId = 999L;

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/customers/" + nonExistentId)
                .then()
                .statusCode(404)
                .body("message", org.hamcrest.Matchers.is("Customer with ID 999 not found"));
    }

    @Test
    @DisplayName("should update customer successfully")
    void shouldUpdateCustomerSuccessfully() {
        String createRequest = """
            {
              "name": "Eduardo Santos",
              "cpf": "111.222.333-44",
              "phoneNumber": "1199999-0000",
              "email": "eduardo.santos@example.com"
            }
            """;

        int id =
                given()
                        .contentType(ContentType.JSON)
                        .body(createRequest)
                        .when()
                        .post("/customers")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        String updateRequest = """
            {
              "name": "Eduardo A. Santos",
              "cpf": "111.222.333-44",
              "phoneNumber": "1191234-5678",
              "email": "eduardo.atualizado@example.com"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/customers/" + id)
                .then()
                .statusCode(200)
                .body("id", org.hamcrest.Matchers.equalTo(id))
                .body("name", org.hamcrest.Matchers.equalTo("Eduardo A. Santos"))
                .body("email", org.hamcrest.Matchers.equalTo("eduardo.atualizado@example.com"));
    }

    @Test
    @DisplayName("should return 404 if trying to update non-existent customer")
    void shouldReturnNotFoundWhenUpdatingNonExistentCustomer() {
        int nonExistentId = 999;

        String updateRequest = """
            {
              "name": "Cliente Inexistente",
              "cpf": "222.333.444-55",
              "phoneNumber": "1190000-0000",
              "email": "nao.existe@example.com"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/customers/" + nonExistentId)
                .then()
                .statusCode(404)
                .body("message", org.hamcrest.Matchers.is("Customer with ID %d not found".formatted(nonExistentId)));
    }

    @Test
    @DisplayName("should return 409 if updating to CPF already used by another customer")
    void shouldReturnConflictWhenUpdatingToExistingCpf() {
        String request1 = """
            {
              "name": "Lucas Carvalho",
              "cpf": "123.123.123-12",
              "phoneNumber": "1198888-1111",
              "email": "lucas@example.com"
            }
            """;

        String request2 = """
            {
              "name": "Paula Mendes",
              "cpf": "456.456.456-45",
              "phoneNumber": "1199999-2222",
              "email": "paula@example.com"
            }
            """;


                given()
                        .contentType(ContentType.JSON)
                        .body(request1)
                        .when()
                        .post("/customers")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        int id2 =
                given()
                        .contentType(ContentType.JSON)
                        .body(request2)
                        .when()
                        .post("/customers")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        String updateToCpfUsedByAnother = """
            {
              "name": "Paula Atualizada",
              "cpf": "123.123.123-12",
              "phoneNumber": "1197777-3333",
              "email": "paula.atualizada@example.com"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updateToCpfUsedByAnother)
                .when()
                .put("/customers/" + id2)
                .then()
                .statusCode(409)
                .body("message", org.hamcrest.Matchers.is("A customer with CPF 123.123.123-12 already exists."));
    }

    @Test
    @DisplayName("should delete customer successfully")
    void shouldDeleteCustomerSuccessfully() {
        String request = """
            {
              "name": "Beatriz Ramos",
              "cpf": "777.888.999-00",
              "phoneNumber": "1196666-4444",
              "email": "beatriz.ramos@example.com"
            }
            """;

        int id =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/customers")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        given()
                .when()
                .delete("/customers/" + id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/customers/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when trying to delete non-existent customer")
    void shouldReturnNotFoundWhenDeletingNonExistentCustomer() {
        int nonExistentId = 999;

        given()
                .when()
                .delete("/customers/" + nonExistentId)
                .then()
                .statusCode(404)
                .body("message", org.hamcrest.Matchers.is("Customer with ID %d not found".formatted(nonExistentId)));
    }


}
