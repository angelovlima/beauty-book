package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.exception.CustomerAlreadyExistsException;
import br.com.beauty_book.customer_management.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import br.com.beauty_book.customer_management.domain.model.value_object.Cpf;
import br.com.beauty_book.customer_management.domain.model.value_object.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UpdateCustomerUseCaseTest {

    private CustomerGateway customerGateway;
    private UpdateCustomerUseCase updateCustomerUseCase;

    @BeforeEach
    void setUp() {
        customerGateway = mock(CustomerGateway.class);
        updateCustomerUseCase = new UpdateCustomerUseCase(customerGateway);
    }

    @Nested
    @DisplayName("When executing customer update")
    class Execute {

        @Test
        @DisplayName("should throw exception if customer does not exist")
        void shouldThrowExceptionIfCustomerNotFound() {
            Long id = 10L;
            Customer updatedData = mock(Customer.class);

            when(customerGateway.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> updateCustomerUseCase.execute(id, updatedData))
                    .isInstanceOf(CustomerNotFoundException.class)
                    .hasMessageContaining(String.valueOf(id));

            verify(customerGateway).findById(id);
            verify(customerGateway, never()).findByCpf(anyString());
            verify(customerGateway, never()).save(any());
        }

        @Test
        @DisplayName("should throw exception if CPF belongs to another customer")
        void shouldThrowExceptionIfCpfBelongsToAnotherCustomer() {
            Long id = 1L;
            Cpf cpf = new Cpf("222.333.444-55");
            Customer updatedData = mock(Customer.class);
            Customer otherCustomer = mock(Customer.class);

            when(updatedData.getCpf()).thenReturn(cpf);
            when(customerGateway.findById(id)).thenReturn(Optional.of(mock(Customer.class)));
            when(customerGateway.findByCpf(cpf.getValue())).thenReturn(Optional.of(otherCustomer));
            when(otherCustomer.getId()).thenReturn(2L);

            assertThatThrownBy(() -> updateCustomerUseCase.execute(id, updatedData))
                    .isInstanceOf(CustomerAlreadyExistsException.class)
                    .hasMessageContaining(cpf.getValue());

            verify(customerGateway).findById(id);
            verify(customerGateway).findByCpf(cpf.getValue());
            verify(customerGateway, never()).save(any());
        }

        @Test
        @DisplayName("should update customer successfully if data is valid")
        void shouldUpdateCustomerSuccessfully() {
            Long id = 1L;
            Cpf cpf = new Cpf("555.666.777-88");
            Email email = new Email("updated@mail.com");

            Customer updatedData = new Customer(id, "Updated Name", cpf, "99999-9999", email);
            Customer existingCustomer = mock(Customer.class);
            Customer savedCustomer = mock(Customer.class);

            when(customerGateway.findById(id)).thenReturn(Optional.of(existingCustomer));
            when(customerGateway.findByCpf(cpf.getValue())).thenReturn(Optional.of(updatedData));
            when(customerGateway.save(any())).thenReturn(savedCustomer);

            Customer result = updateCustomerUseCase.execute(id, updatedData);

            assertThat(result).isEqualTo(savedCustomer);

            verify(customerGateway).findById(id);
            verify(customerGateway).findByCpf(cpf.getValue());
            verify(customerGateway).save(any());
        }
    }
}
