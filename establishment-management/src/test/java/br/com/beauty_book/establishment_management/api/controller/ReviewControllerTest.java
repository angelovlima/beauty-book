package br.com.beauty_book.establishment_management.api.controller;

import br.com.beauty_book.establishment_management.api.dto.ReviewApiResponse;
import br.com.beauty_book.establishment_management.domain.gateway.BookingHistoryVerificationGateway;
import br.com.beauty_book.establishment_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.CustomerDto;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.ReviewRepository;
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

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ReviewControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @MockBean
    private BookingHistoryVerificationGateway bookingVerificationGateway;

    @MockBean
    private CustomerGateway customerGateway;

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
        reviewRepository.deleteAll();
        establishmentRepository.deleteAll();
    }

    @Test
    @DisplayName("should create review successfully")
    void shouldCreateReviewSuccessfully() {
        var establishment = new EstablishmentJpaEntity("Studio A", "Av. Central, 100", "https://example.com/image.jpg");
        establishment = establishmentRepository.save(establishment);

        when(customerGateway.findById(1L)).thenReturn(Optional.of(new CustomerDto(1L, "Carlos", "carlos@email.com")));
        when(bookingVerificationGateway.customerHasCompletedBooking(1L, establishment.getId())).thenReturn(true);

        String request = String.format("""
            {
                "customerId": 1,
                "establishmentId": %d,
                "stars": 5,
                "comment": "Atendimento excelente"
            }
        """, establishment.getId());

        ReviewApiResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/reviews")
                .then()
                .statusCode(201)
                .extract()
                .as(ReviewApiResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.stars()).isEqualTo(5);
        assertThat(response.comment()).isEqualTo("Atendimento excelente");
    }

    @Test
    @DisplayName("should not allow duplicate review")
    void shouldNotAllowDuplicateReview() {
        var establishment = new EstablishmentJpaEntity("Studio B", "Rua das Flores, 200", "https://example.com/image.jpg");
        establishment = establishmentRepository.save(establishment);

        when(customerGateway.findById(1L)).thenReturn(Optional.of(new CustomerDto(1L, "Carlos", "carlos@email.com")));
        when(bookingVerificationGateway.customerHasCompletedBooking(1L, establishment.getId())).thenReturn(true);

        String request = String.format("""
        {
            "customerId": 1,
            "establishmentId": %d,
            "stars": 4,
            "comment": "Ótimo atendimento"
        }
    """, establishment.getId());

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/reviews")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/reviews")
                .then()
                .statusCode(409);
    }

    @Test
    @DisplayName("should return 400 if customer not found")
    void shouldReturn400IfCustomerNotFound() {
        var establishment = new EstablishmentJpaEntity("Studio C", "Rua das Palmeiras, 123", "https://example.com/image.jpg");
        establishment = establishmentRepository.save(establishment);

        when(customerGateway.findById(1L)).thenReturn(Optional.empty());

        String request = String.format("""
        {
            "customerId": 1,
            "establishmentId": %d,
            "stars": 5,
            "comment": "Ótimo serviço"
        }
    """, establishment.getId());

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/reviews")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("should return 400 if customer has no completed booking")
    void shouldReturn400IfNoCompletedBooking() {
        var establishment = new EstablishmentJpaEntity("Studio D", "Av. Brasil, 789", "https://example.com/image.jpg");
        establishment = establishmentRepository.save(establishment);

        when(customerGateway.findById(1L)).thenReturn(Optional.of(new CustomerDto(1L, "Carlos", "carlos@email.com")));
        when(bookingVerificationGateway.customerHasCompletedBooking(1L, establishment.getId())).thenReturn(false);

        String request = String.format("""
        {
            "customerId": 1,
            "establishmentId": %d,
            "stars": 4,
            "comment": "Legal, mas poderia melhorar"
        }
    """, establishment.getId());

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/reviews")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("should list reviews by establishment")
    void shouldListReviewsByEstablishment() {
        var establishment = new EstablishmentJpaEntity("Studio E", "Rua Nova, 456", "https://example.com/image.jpg");
        establishment = establishmentRepository.save(establishment);

        reviewRepository.saveAll(List.of(
                new br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ReviewJpaEntity(1L, establishment, 5, "Excelente"),
                new br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ReviewJpaEntity(2L, establishment, 4, "Bom")
        ));

        var response = given()
                .when()
                .get("/reviews/establishment/" + establishment.getId())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReviewApiResponse.class);

        assertThat(response).hasSize(2);
        assertThat(response).extracting("stars").containsExactlyInAnyOrder(5, 4);
    }

}
