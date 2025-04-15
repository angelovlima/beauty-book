package br.com.beauty_book.customer_management.api.dto.mapper;

import br.com.beauty_book.customer_management.api.dto.CreateCustomerApiRequest;
import br.com.beauty_book.customer_management.api.dto.CustomerApiResponse;
import br.com.beauty_book.customer_management.api.dto.UpdateCustomerApiRequest;
import br.com.beauty_book.customer_management.domain.model.Customer;
import br.com.beauty_book.customer_management.domain.model.value_object.Cpf;
import br.com.beauty_book.customer_management.domain.model.value_object.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerApiMapperTest {

    @Nested
    @DisplayName("Mapping from CreateCustomerApiRequest to Customer domain")
    class FromCreateRequest {

        @Test
        @DisplayName("should map all fields correctly")
        void shouldMapCreateRequestToCustomer() {
            CreateCustomerApiRequest request = new CreateCustomerApiRequest(
                    "Carlos Souza",
                    "123.456.789-00",
                    "1199999-9999",
                    "carlos.souza@example.com"
            );

            Customer result = CustomerApiMapper.toDomain(request);

            assertThat(result.getId()).isNull();
            assertThat(result.getName()).isEqualTo("Carlos Souza");
            assertThat(result.getCpf().getValue()).isEqualTo("123.456.789-00");
            assertThat(result.getPhoneNumber()).isEqualTo("1199999-9999");
            assertThat(result.getEmail().getValue()).isEqualTo("carlos.souza@example.com");
        }

        @Test
        @DisplayName("should map null email correctly")
        void shouldMapCreateRequestWithNullEmail() {
            CreateCustomerApiRequest request = new CreateCustomerApiRequest(
                    "Fernanda Alves",
                    "987.654.321-00",
                    "1198888-8888",
                    null
            );

            Customer result = CustomerApiMapper.toDomain(request);

            assertThat(result.getEmail()).isNull();
        }
    }

    @Nested
    @DisplayName("Mapping from UpdateCustomerApiRequest to Customer domain")
    class FromUpdateRequest {

        @Test
        @DisplayName("should map all fields correctly with given ID")
        void shouldMapUpdateRequestToCustomer() {
            UpdateCustomerApiRequest request = new UpdateCustomerApiRequest(
                    "Ana Clara",
                    "555.666.777-88",
                    "1197777-7777",
                    "ana.clara@example.com"
            );

            Customer result = CustomerApiMapper.toDomain(request, 15L);

            assertThat(result.getId()).isEqualTo(15L);
            assertThat(result.getName()).isEqualTo("Ana Clara");
            assertThat(result.getCpf().getValue()).isEqualTo("555.666.777-88");
            assertThat(result.getPhoneNumber()).isEqualTo("1197777-7777");
            assertThat(result.getEmail().getValue()).isEqualTo("ana.clara@example.com");
        }

        @Test
        @DisplayName("should map null email correctly")
        void shouldMapUpdateRequestWithNullEmail() {
            UpdateCustomerApiRequest request = new UpdateCustomerApiRequest(
                    "João Mendes",
                    "111.222.333-44",
                    "1196666-6666",
                    null
            );

            Customer result = CustomerApiMapper.toDomain(request, 30L);

            assertThat(result.getId()).isEqualTo(30L);
            assertThat(result.getEmail()).isNull();
        }
    }

    @Nested
    @DisplayName("Mapping from Customer domain to CustomerApiResponse")
    class ToResponse {

        @Test
        @DisplayName("should map all fields correctly")
        void shouldMapCustomerToResponse() {
            Customer customer = new Customer(
                    20L,
                    "Mariana Ribeiro",
                    new Cpf("321.654.987-00"),
                    "1195555-5555",
                    new Email("mariana.ribeiro@example.com")
            );

            CustomerApiResponse response = CustomerApiMapper.toResponse(customer);

            assertThat(response.id()).isEqualTo(20L);
            assertThat(response.name()).isEqualTo("Mariana Ribeiro");
            assertThat(response.cpf()).isEqualTo("321.654.987-00");
            assertThat(response.phoneNumber()).isEqualTo("1195555-5555");
            assertThat(response.email()).isEqualTo("mariana.ribeiro@example.com");
        }

        @Test
        @DisplayName("should map null email correctly")
        void shouldMapCustomerWithNullEmailToResponse() {
            Customer customer = new Customer(
                    21L,
                    "Eduardo Lima",
                    new Cpf("444.555.666-77"),
                    "1194444-4444",
                    null
            );

            CustomerApiResponse response = CustomerApiMapper.toResponse(customer);

            assertThat(response.email()).isNull();
        }
    }
}
