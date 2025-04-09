package br.com.beauty_book.customer_management.api.dto.mapper;

import br.com.beauty_book.customer_management.api.dto.CreateCustomerApiRequest;
import br.com.beauty_book.customer_management.api.dto.CustomerApiResponse;
import br.com.beauty_book.customer_management.api.dto.UpdateCustomerApiRequest;
import br.com.beauty_book.customer_management.domain.model.Customer;
import br.com.beauty_book.customer_management.domain.model.value_object.Cpf;
import br.com.beauty_book.customer_management.domain.model.value_object.Email;

public class CustomerApiMapper {

    private CustomerApiMapper() {}

    public static Customer toDomain(CreateCustomerApiRequest request) {
        return new Customer(
                null,
                request.name(),
                new Cpf(request.cpf()),
                request.phoneNumber(),
                request.email() != null ? new Email(request.email()) : null
        );
    }

    public static Customer toDomain(UpdateCustomerApiRequest request, Long id) {
        return new Customer(
                id,
                request.name(),
                new Cpf(request.cpf()),
                request.phoneNumber(),
                request.email() != null ? new Email(request.email()) : null
        );
    }

    public static CustomerApiResponse toResponse(Customer customer) {
        return new CustomerApiResponse(
                customer.getId(),
                customer.getName(),
                customer.getCpf().getValue(),
                customer.getPhoneNumber(),
                customer.getEmail() != null ? customer.getEmail().getValue() : null
        );
    }
}
