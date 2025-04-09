package br.com.beauty_book.customer_management.domain.usecase;

import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GetAllCustomersUseCase {

    private final CustomerGateway customerGateway;

    public GetAllCustomersUseCase(CustomerGateway customerGateway) {
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    public List<Customer> execute() {
        return customerGateway.findAll();
    }
}
