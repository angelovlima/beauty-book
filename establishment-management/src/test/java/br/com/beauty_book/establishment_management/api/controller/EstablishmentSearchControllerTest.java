package br.com.beauty_book.establishment_management.api.controller;

import br.com.beauty_book.establishment_management.api.dto.EstablishmentApiResponse;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class EstablishmentSearchControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EstablishmentRepository repository;

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
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("eureka.client.enabled", () -> false);
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        repository.deleteAll();

        EstablishmentJpaEntity entity = new EstablishmentJpaEntity(
                "Studio Beleza Pura",
                "Av. Paulista, 1000, São Paulo - SP",
                "https://example.com/image.jpg"
        );

        repository.save(entity);
    }

    @Test
    @DisplayName("should search establishments using filter")
    void shouldSearchWithFilter() {
        List<EstablishmentApiResponse> results = given()
                .contentType(ContentType.JSON)
                .queryParam("name", "Studio")
                .queryParam("location", "Paulista")
                .when()
                .get("/establishments/search")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", EstablishmentApiResponse.class);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).name()).contains("Studio");
        assertThat(results.get(0).address()).contains("Paulista");
    }
}
