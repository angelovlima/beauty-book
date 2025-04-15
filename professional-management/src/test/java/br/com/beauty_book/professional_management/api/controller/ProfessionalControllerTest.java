package br.com.beauty_book.professional_management.api.controller;

import br.com.beauty_book.professional_management.api.dto.ProfessionalApiResponse;
import br.com.beauty_book.professional_management.infra.gateway.integration.client.ServiceOfferedClient;
import br.com.beauty_book.professional_management.infra.gateway.integration.dto.ServiceOfferedDto;
import br.com.beauty_book.professional_management.infra.gateway.jpa.repository.ProfessionalRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProfessionalControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProfessionalRepository repository;

    @MockBean
    private ServiceOfferedClient serviceOfferedClient;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("eureka.client.enabled", () -> false);
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        repository.deleteAll();

        when(serviceOfferedClient.findById(1L))
                .thenReturn(new ServiceOfferedDto(
                        1L,
                        "Corte",
                        "Corte feminino",
                        BigDecimal.valueOf(50.0),
                        45
                ));
    }

    @Test
    void shouldCreateProfessionalSuccessfully() {
        String request = """
        {
            "name": "Bianca",
            "cpf": "123.456.789-00",
            "phoneNumber": "11999999999",
            "email": "bianca@email.com",
            "availabilityList": [
                {"dayOfWeek": 1, "startTime": "09:00", "endTime": "17:00"}
            ],
            "services": [
                {"id": 1}
            ]
        }
        """;

        ProfessionalApiResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/professionals")
                .then()
                .statusCode(201)
                .extract()
                .as(ProfessionalApiResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Bianca");
        assertThat(response.cpf()).isEqualTo("123.456.789-00");
    }

    @Test
    @DisplayName("should return list of professionals")
    void shouldReturnListOfProfessionals() {
        String request = """
        {
          "name": "Patrícia",
          "cpf": "111.222.333-44",
          "phoneNumber": "+55 11 91234-5678",
          "email": "patricia@email.com",
          "availabilityList": [
            { "dayOfWeek": 1, "startTime": "09:00", "endTime": "18:00" }
          ],
          "services": [
            { "id": 1 }
          ]
        }
    """;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/professionals")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/professionals")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void shouldFindProfessionalById() {
        String request = """
            {
              "name": "Pedro",
              "cpf": "987.654.321-00",
              "phoneNumber": "11911112222",
              "email": "pedro@email.com",
              "availabilityList": [
                { "dayOfWeek": 1, "startTime": "09:00", "endTime": "17:00" }
              ],
              "services": [
                { "id": 1 }
              ]
            }
            """;

        ProfessionalApiResponse created = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/professionals")
                .then()
                .statusCode(201)
                .extract()
                .as(ProfessionalApiResponse.class);

        given()
                .get("/professionals/" + created.id())
                .then()
                .statusCode(200)
                .body("name", equalTo("Pedro"))
                .body("cpf", equalTo("987.654.321-00"));
    }

    @Test
    void shouldFindProfessionalByCpf() {
        String request = """
            {
              "name": "Lucas",
              "cpf": "999.888.777-66",
              "phoneNumber": "11922223333",
              "email": "lucas@email.com",
              "availabilityList": [
                { "dayOfWeek": 2, "startTime": "08:00", "endTime": "16:00" }
              ],
              "services": [
                { "id": 1 }
              ]
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/professionals")
                .then()
                .statusCode(201);

        given()
                .get("/professionals/cpf/999.888.777-66")
                .then()
                .statusCode(200)
                .body("email", equalTo("lucas@email.com"));
    }

    @Test
    void shouldUpdateProfessionalSuccessfully() {
        String request = """
            {
              "name": "Julia",
              "cpf": "222.333.444-55",
              "phoneNumber": "11933334444",
              "email": "julia@email.com",
              "availabilityList": [
                { "dayOfWeek": 4, "startTime": "10:00", "endTime": "19:00" }
              ],
              "services": [
                { "id": 1 }
              ]
            }
            """;

        ProfessionalApiResponse created = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/professionals")
                .then()
                .statusCode(201)
                .extract()
                .as(ProfessionalApiResponse.class);

        String update = """
            {
              "name": "Juliana",
              "cpf": "222.333.444-55",
              "phoneNumber": "11900001111",
              "email": "juliana@email.com",
              "availabilityList": [
                { "dayOfWeek": 4, "startTime": "12:00", "endTime": "20:00" }
              ],
              "services": [
                { "id": 1 }
              ]
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .put("/professionals/" + created.id())
                .then()
                .statusCode(200)
                .body("name", equalTo("Juliana"));
    }

    @Test
    void shouldDeleteProfessionalSuccessfully() {
        String request = """
            {
              "name": "Carlos",
              "cpf": "333.222.111-00",
              "phoneNumber": "11955556666",
              "email": "carlos@email.com",
              "availabilityList": [
                { "dayOfWeek": 5, "startTime": "07:00", "endTime": "15:00" }
              ],
              "services": [
                { "id": 1 }
              ]
            }
            """;

        ProfessionalApiResponse created = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/professionals")
                .then()
                .statusCode(201)
                .extract()
                .as(ProfessionalApiResponse.class);

        given()
                .delete("/professionals/" + created.id())
                .then()
                .statusCode(204);
    }

    @Test
    void shouldReturnAvailabilitiesByProfessionalId() {
        String request = """
            {
              "name": "Roberta",
              "cpf": "123.789.456-00",
              "phoneNumber": "11977778888",
              "email": "roberta@email.com",
              "availabilityList": [
                { "dayOfWeek": 0, "startTime": "10:00", "endTime": "18:00" }
              ],
              "services": [
                { "id": 1 }
              ]
            }
            """;

        ProfessionalApiResponse created = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/professionals")
                .then()
                .statusCode(201)
                .extract()
                .as(ProfessionalApiResponse.class);

        given()
                .get("/professionals/" + created.id() + "/availabilities")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}
