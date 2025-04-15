package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.exception.CustomerAlreadyExistsException;
import br.com.beauty_book.customer_management.domain.exception.InvalidCpfException;
import br.com.beauty_book.customer_management.domain.exception.InvalidEmailException;
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

class CreateCustomerUseCaseTest {

    private CustomerGateway customerGateway;
    private CreateCustomerUseCase createCustomerUseCase;

    @BeforeEach
    void setUp() {
        customerGateway = mock(CustomerGateway.class);
        createCustomerUseCase = new CreateCustomerUseCase(customerGateway);
    }

    @Nested
    @DisplayName("When executing customer creation")
    class Execute {

        @Test
        @DisplayName("should throw exception if CPF already exists")
        void shouldThrowExceptionIfCpfExists() {
            Cpf cpf = new Cpf("123.456.789-00");
            Customer existingCustomer = mock(Customer.class);

            when(customerGateway.findByCpf(cpf.getValue())).thenReturn(Optional.of(existingCustomer));

            Customer newCustomer = mock(Customer.class);
            when(newCustomer.getCpf()).thenReturn(cpf);

            assertThatThrownBy(() -> createCustomerUseCase.execute(newCustomer))
                    .isInstanceOf(CustomerAlreadyExistsException.class)
                    .hasMessageContaining(cpf.getValue());

            verify(customerGateway).findByCpf(cpf.getValue());
            verify(customerGateway, never()).save(any());
        }

        @Test
        @DisplayName("should throw exception when creating customer with invalid CPF")
        void shouldThrowExceptionForInvalidCpf() {
            assertThatThrownBy(() -> new Cpf("12345678900"))
                    .isInstanceOf(InvalidCpfException.class)
                    .hasMessageContaining("12345678900");
        }

        @Test
        @DisplayName("should throw exception when creating customer with invalid email")
        void shouldThrowExceptionForInvalidEmail() {
            assertThatThrownBy(() -> new Email("invalid-email"))
                    .isInstanceOf(InvalidEmailException.class)
                    .hasMessageContaining("invalid-email");
        }

        @Test
        @DisplayName("should create customer successfully if CPF is unique")
        void shouldCreateCustomerSuccessfully() {
            Cpf cpf = new Cpf("111.222.333-44");
            Customer newCustomer = mock(Customer.class);
            Customer savedCustomer = mock(Customer.class);

            when(newCustomer.getCpf()).thenReturn(cpf);
            when(customerGateway.findByCpf(cpf.getValue())).thenReturn(Optional.empty());
            when(customerGateway.save(newCustomer)).thenReturn(savedCustomer);

            Customer result = createCustomerUseCase.execute(newCustomer);

            assertThat(result).isEqualTo(savedCustomer);

            verify(customerGateway).findByCpf(cpf.getValue());
            verify(customerGateway).save(newCustomer);
        }
    }
}
