package br.com.beauty_book.booking.api.controller;

import br.com.beauty_book.booking.api.dto.BookingApiResponse;
import br.com.beauty_book.booking.infra.gateway.integration.client.CustomerClient;
import br.com.beauty_book.booking.infra.gateway.integration.client.EstablishmentClient;
import br.com.beauty_book.booking.infra.gateway.integration.client.ProfessionalClient;
import br.com.beauty_book.booking.infra.gateway.integration.client.ServiceOfferedClient;
import br.com.beauty_book.booking.infra.gateway.integration.dto.*;
import br.com.beauty_book.booking.infra.gateway.jpa.repository.BookingRepository;
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
import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class BookingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookingRepository repository;

    @MockBean
    private CustomerClient customerClient;

    @MockBean
    private ProfessionalClient professionalClient;

    @MockBean
    private EstablishmentClient establishmentClient;

    @MockBean
    private ServiceOfferedClient serviceOfferedClient;

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
    void setup() {
        RestAssured.port = port;
        repository.deleteAll();

        when(customerClient.findById(1L))
                .thenReturn(new CustomerDto(1L, "Joana", "joana@email.com"));

        when(professionalClient.findById(2L))
                .thenReturn(new ProfessionalDto(
                        2L,
                        "Marcos",
                        "123.456.789-00",
                        "1199999-9999",
                        "marcos@email.com"
                ));

        when(establishmentClient.findById(3L))
                .thenReturn(new EstablishmentDto(
                        3L,
                        "Studio X",
                        "Av. Central",
                        "https://example.com/image.jpg"
                ));

        when(establishmentClient.getOperatingHoursByEstablishmentId(3L))
                .thenReturn(List.of(
                        new OperatingHourDto(0, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new OperatingHourDto(1, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new OperatingHourDto(2, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new OperatingHourDto(3, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new OperatingHourDto(4, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new OperatingHourDto(5, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new OperatingHourDto(6, LocalTime.of(8, 0), LocalTime.of(18, 0))
                ));

        when(professionalClient.getAvailabilityByProfessionalId(2L))
                .thenReturn(List.of(
                        new AvailabilityDto(0, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new AvailabilityDto(1, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new AvailabilityDto(2, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new AvailabilityDto(3, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new AvailabilityDto(4, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new AvailabilityDto(5, LocalTime.of(8, 0), LocalTime.of(18, 0)),
                        new AvailabilityDto(6, LocalTime.of(8, 0), LocalTime.of(18, 0))
                ));

        when(serviceOfferedClient.findById(4L))
                .thenReturn(new ServiceOfferedDto(
                        4L,
                        3L,
                        "Corte",
                        "Corte feminino",
                        new BigDecimal("50.00"),
                        45
                ));
    }

    @Test
    @DisplayName("should create booking successfully")
    void shouldCreateBookingSuccessfully() {
        String request = """
            {
              "customerId": 1,
              "professionalId": 2,
              "establishmentId": 3,
              "serviceId": 4,
              "bookingDate": "2025-04-25",
              "startTime": "10:00"
            }
        """;

        BookingApiResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .as(BookingApiResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.customerId()).isEqualTo(1L);
        assertThat(response.professionalId()).isEqualTo(2L);
        assertThat(response.status().name()).isEqualTo("SCHEDULED");
    }

    @Test
    @DisplayName("should cancel booking successfully")
    void shouldCancelBookingSuccessfully() {
        String request = """
        {
          "customerId": 1,
          "professionalId": 2,
          "establishmentId": 3,
          "serviceId": 4,
          "bookingDate": "2025-04-25",
          "startTime": "10:00"
        }
    """;

        BookingApiResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .as(BookingApiResponse.class);

        given()
                .when()
                .patch("/bookings/" + response.id() + "/cancel")
                .then()
                .statusCode(200)
                .body("id", equalTo(response.id().intValue()))
                .body("status", equalTo("CANCELLED"));
    }

    @Test
    @DisplayName("should reschedule booking successfully")
    void shouldRescheduleBookingSuccessfully() {
        String request = """
        {
          "customerId": 1,
          "professionalId": 2,
          "establishmentId": 3,
          "serviceId": 4,
          "bookingDate": "2025-04-25",
          "startTime": "10:00"
        }
    """;

        BookingApiResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .as(BookingApiResponse.class);

        String reschedule = """
        {
          "newBookingDate": "2025-04-26",
          "newStartTime": "11:00"
        }
    """;

        given()
                .contentType(ContentType.JSON)
                .body(reschedule)
                .when()
                .patch("/bookings/" + response.id() + "/reschedule")
                .then()
                .statusCode(200)
                .body("bookingDate", equalTo("2025-04-26"))
                .body("startTime", equalTo("11:00"));
    }

    @Test
    @DisplayName("should complete booking successfully")
    void shouldCompleteBookingSuccessfully() {
        String request = """
        {
          "customerId": 1,
          "professionalId": 2,
          "establishmentId": 3,
          "serviceId": 4,
          "bookingDate": "2025-04-25",
          "startTime": "10:00"
        }
    """;

        BookingApiResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .as(BookingApiResponse.class);

        given()
                .when()
                .patch("/bookings/" + response.id() + "/complete")
                .then()
                .statusCode(200)
                .body("status", equalTo("COMPLETED"));
    }

    @Test
    @DisplayName("should return list of bookings")
    void shouldReturnListOfBookings() {
        String request = """
        {
          "customerId": 1,
          "professionalId": 2,
          "establishmentId": 3,
          "serviceId": 4,
          "bookingDate": "2025-04-25",
          "startTime": "10:00"
        }
    """;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/bookings")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    @DisplayName("should return true for completed booking verification")
    void shouldReturnTrueIfCustomerHasCompletedBooking() {
        String request = """
        {
          "customerId": 1,
          "professionalId": 2,
          "establishmentId": 3,
          "serviceId": 4,
          "bookingDate": "2025-04-25",
          "startTime": "10:00"
        }
    """;

        BookingApiResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)
                .extract()
                .as(BookingApiResponse.class);

        given()
                .patch("/bookings/" + response.id() + "/complete")
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/bookings/customer/1/has-completed/establishment/3")
                .then()
                .statusCode(200)
                .body("hasCompletedBooking", equalTo(true));
    }

}

