package br.com.beauty_book.establishment_management.api.controller;

import br.com.beauty_book.establishment_management.api.dto.EstablishmentApiResponse;
import br.com.beauty_book.establishment_management.infra.gateway.integration.client.ProfessionalClient;
import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.ProfessionalDto;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
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

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class EstablishmentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EstablishmentRepository repository;

    @MockBean
    private ProfessionalClient professionalClient;


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
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("eureka.client.enabled", () -> false);
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        repository.deleteAll();
        when(professionalClient.findById(10L))
                .thenReturn(new ProfessionalDto(
                        10L,
                        "Patrícia",
                        "123.456.789-10",
                        "11999999999",
                        "patricia@studio.com"
                ));
    }

    @Test
    @DisplayName("should create establishment successfully")
    void shouldCreateEstablishmentSuccessfully() {
        String request = """
        {
            "name": "Studio Fênix",
            "address": "Rua Central, 123",
            "photoUrl": "https://imagem.com/foto.png",
            "operatingHours": [
                { "dayOfWeek": 1, "startTime": "08:00", "endTime": "18:00" }
            ],
            "services": [
                { "name": "Corte", "description": "Corte feminino", "price": 60.0, "durationMinutes": 30 }
            ],
            "professionals": [
                { "professionalId": 10 }
            ]
        }
        """;

        EstablishmentApiResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/establishments")
                .then()
                .statusCode(201)
                .extract()
                .as(EstablishmentApiResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Studio Fênix");
        assertThat(response.address()).isEqualTo("Rua Central, 123");
    }

    @Test
    @DisplayName("should return list of establishments")
    void shouldReturnListOfEstablishments() {
        String request = """
        {
            "name": "Studio Fênix",
            "address": "Rua Central, 123",
            "photoUrl": "https://imagem.com/foto.png",
            "operatingHours": [
                { "dayOfWeek": 1, "startTime": "08:00", "endTime": "18:00" }
            ],
            "services": [
                { "name": "Corte", "description": "Corte feminino", "price": 60.0, "durationMinutes": 30 }
            ],
            "professionals": [
                { "professionalId": 10 }
            ]
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/establishments")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/establishments")
                .then()
                .statusCode(200)
                .body("size()", org.hamcrest.Matchers.greaterThan(0));
    }
}
