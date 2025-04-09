package br.com.beauty_book.customer_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.customer_management.domain.model.Customer;
import br.com.beauty_book.customer_management.domain.model.value_object.Cpf;
import br.com.beauty_book.customer_management.domain.model.value_object.Email;
import br.com.beauty_book.customer_management.infra.gateway.jpa.entity.CustomerJpaEntity;

public class CustomerJpaMapper {

    private CustomerJpaMapper() {
    }

    public static Customer toDomain(CustomerJpaEntity entity) {
        if (entity == null) return null;

        return new Customer(
                entity.getId(),
                entity.getName(),
                new Cpf(entity.getCpf()),
                entity.getPhoneNumber(),
                entity.getEmail() != null ? new Email(entity.getEmail()) : null
        );

    }

    public static CustomerJpaEntity toEntity(Customer domain) {
        if (domain == null) return null;

        return CustomerJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .cpf(domain.getCpf().getValue())
                .phoneNumber(domain.getPhoneNumber())
                .email(domain.getEmail() != null ? domain.getEmail().getValue() : null)
                .build();
    }
}
