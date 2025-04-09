package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.exception.CustomerAlreadyExistsException;
import br.com.beauty_book.customer_management.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UpdateCustomerUseCase {

    private final CustomerGateway customerGateway;

    public UpdateCustomerUseCase(CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    public Customer execute(Long id, Customer updatedData) {
        customerGateway.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customerGateway.findByCpf(updatedData.getCpf().getValue())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(conflict -> {
                    throw new CustomerAlreadyExistsException(updatedData.getCpf().getValue());
                });

        Customer toSave = new Customer(
                id,
                updatedData.getName(),
                updatedData.getCpf(),
                updatedData.getPhoneNumber(),
                updatedData.getEmail()
        );

        return customerGateway.save(toSave);
    }
}
