package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetAllCustomersUseCaseTest {

    private CustomerGateway customerGateway;
    private GetAllCustomersUseCase getAllCustomersUseCase;

    @BeforeEach
    void setUp() {
        customerGateway = mock(CustomerGateway.class);
        getAllCustomersUseCase = new GetAllCustomersUseCase(customerGateway);
    }

    @Nested
    @DisplayName("When executing customer list retrieval")
    class Execute {

        @Test
        @DisplayName("should return all customers from gateway")
        void shouldReturnAllCustomers() {
            Customer c1 = mock(Customer.class);
            Customer c2 = mock(Customer.class);
            List<Customer> customerList = List.of(c1, c2);

            when(customerGateway.findAll()).thenReturn(customerList);

            List<Customer> result = getAllCustomersUseCase.execute();

            assertThat(result).containsExactlyElementsOf(customerList);
            verify(customerGateway).findAll();
        }

        @Test
        @DisplayName("should return empty list if no customers found")
        void shouldReturnEmptyListIfNoneExist() {
            when(customerGateway.findAll()).thenReturn(List.of());

            List<Customer> result = getAllCustomersUseCase.execute();

            assertThat(result).isEmpty();
            verify(customerGateway).findAll();
        }
    }
}
