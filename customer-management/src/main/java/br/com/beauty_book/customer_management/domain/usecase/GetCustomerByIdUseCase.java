package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GetCustomerByIdUseCase {

    private final CustomerGateway customerGateway;

    public GetCustomerByIdUseCase(CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    public Customer execute(Long id) {
        return customerGateway.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
