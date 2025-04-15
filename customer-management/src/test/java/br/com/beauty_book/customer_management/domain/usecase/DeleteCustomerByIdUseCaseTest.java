package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteCustomerByIdUseCaseTest {

    private CustomerGateway customerGateway;
    private DeleteCustomerByIdUseCase deleteCustomerByIdUseCase;

    @BeforeEach
    void setUp() {
        customerGateway = mock(CustomerGateway.class);
        deleteCustomerByIdUseCase = new DeleteCustomerByIdUseCase(customerGateway);
    }

    @Nested
    @DisplayName("When executing customer deletion by ID")
    class Execute {

        @Test
        @DisplayName("should delete customer if found")
        void shouldDeleteCustomerIfFound() {
            Long id = 1L;
            Customer customer = mock(Customer.class);

            when(customerGateway.findById(id)).thenReturn(Optional.of(customer));

            deleteCustomerByIdUseCase.execute(id);

            verify(customerGateway).findById(id);
            verify(customerGateway).deleteById(id);
        }

        @Test
        @DisplayName("should throw exception if customer not found")
        void shouldThrowExceptionIfCustomerNotFound() {
            Long id = 42L;

            when(customerGateway.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> deleteCustomerByIdUseCase.execute(id))
                    .isInstanceOf(CustomerNotFoundException.class)
                    .hasMessageContaining(String.valueOf(id));

            verify(customerGateway).findById(id);
            verify(customerGateway, never()).deleteById(any());
        }
    }
}
