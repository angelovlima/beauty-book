package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.exception.CustomerAlreadyExistsException;
import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CreateCustomerUseCase {

    private final CustomerGateway customerGateway;

    public CreateCustomerUseCase(CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    public Customer execute(Customer customer) {
        customerGateway.findByCpf(customer.getCpf().getValue())
                .ifPresent(existing -> {
                    throw new CustomerAlreadyExistsException(customer.getCpf().getValue());
                });

        return customerGateway.save(customer);
    }
}
