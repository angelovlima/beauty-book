package br.com.beauty_book.establishment_management.api.controller;

import br.com.beauty_book.establishment_management.api.dto.ServiceOfferedApiResponse;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ServiceOfferedJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.ServiceOfferedRepository;
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

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ServiceOfferedControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @Autowired
    private ServiceOfferedRepository serviceRepository;

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

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        serviceRepository.deleteAll();
        establishmentRepository.deleteAll();
    }

    @Test
    @DisplayName("should return service offered by id")
    void shouldReturnServiceOfferedById() {
        var establishment = new EstablishmentJpaEntity("Studio A", "Av. Central", "https://example.com/image.jpg");
        establishment = establishmentRepository.save(establishment);

        var service = new ServiceOfferedJpaEntity(
                "Corte",
                "Corte feminino",
                new BigDecimal("50.00"),
                45,
                establishment
        );
        service = serviceRepository.save(service);

        ServiceOfferedApiResponse response = given()
                .when()
                .get("/services/" + service.getId())
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(ServiceOfferedApiResponse.class);

        assertThat(response.id()).isEqualTo(service.getId());
        assertThat(response.name()).isEqualTo("Corte");
        assertThat(response.price()).isEqualTo(new BigDecimal("50.00"));
        assertThat(response.durationMinutes()).isEqualTo(45);
    }
}
