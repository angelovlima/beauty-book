package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetCustomerByIdUseCaseTest {

    private CustomerGateway customerGateway;
    private GetCustomerByIdUseCase getCustomerByIdUseCase;

    @BeforeEach
    void setUp() {
        customerGateway = mock(CustomerGateway.class);
        getCustomerByIdUseCase = new GetCustomerByIdUseCase(customerGateway);
    }

    @Nested
    @DisplayName("When executing customer retrieval by ID")
    class Execute {

        @Test
        @DisplayName("should return customer if found")
        void shouldReturnCustomerIfFound() {
            Long id = 1L;
            Customer customer = mock(Customer.class);

            when(customerGateway.findById(id)).thenReturn(Optional.of(customer));

            Customer result = getCustomerByIdUseCase.execute(id);

            assertThat(result).isEqualTo(customer);
            verify(customerGateway).findById(id);
        }

        @Test
        @DisplayName("should throw exception if customer not found")
        void shouldThrowExceptionIfNotFound() {
            Long id = 99L;

            when(customerGateway.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> getCustomerByIdUseCase.execute(id))
                    .isInstanceOf(CustomerNotFoundException.class)
                    .hasMessageContaining(String.valueOf(id));

            verify(customerGateway).findById(id);
        }
    }
}
