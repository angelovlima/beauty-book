package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DeleteCustomerByIdUseCase {

    private final CustomerGateway customerGateway;

    public DeleteCustomerByIdUseCase(CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    public void execute(Long id) {
        customerGateway.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customerGateway.deleteById(id);
    }
}
