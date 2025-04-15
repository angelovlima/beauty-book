package br.com.beauty_book.booking.api.controller;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.domain.service.CalendarSyncService;
import br.com.beauty_book.booking.infra.gateway.jpa.mapper.BookingJpaMapper;
import br.com.beauty_book.booking.infra.gateway.jpa.repository.BookingRepository;
import io.restassured.RestAssured;
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

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CalendarDownloadControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CalendarSyncService calendarSyncService;

    @Autowired
    private BookingJpaMapper mapper;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("calendar_test_db")
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
        bookingRepository.deleteAll();

        Booking booking = new Booking(
                1L,
                100L,
                200L,
                300L,
                400L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                BookingStatus.SCHEDULED
        );

        bookingRepository.save(mapper.toEntity(booking));
    }


    @Test
    @DisplayName("should download .ics calendar for customer")
    void shouldDownloadCalendarForCustomer() {
        given()
                .when()
                .get("/bookings/calendar-download/customer/100")
                .then()
                .statusCode(200)
                .contentType("text/calendar")
                .header("Content-Disposition", containsString("agenda-customer-100.ics"))
                .body(containsString("BEGIN:VEVENT"))
                .body(containsString("SUMMARY:Agendamento - Servico 400"));
    }

    @Test
    @DisplayName("should download .ics calendar for professional")
    void shouldDownloadCalendarForProfessional() {
        given()
                .when()
                .get("/bookings/calendar-download/professional/200")
                .then()
                .statusCode(200)
                .contentType("text/calendar")
                .header("Content-Disposition", containsString("agenda-professional-200.ics"))
                .body(containsString("BEGIN:VEVENT"))
                .body(containsString("UID:booking-1@beautybook.com"));
    }
}
