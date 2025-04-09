package br.com.beauty_book.customer_management.domain.model;

import br.com.beauty_book.customer_management.domain.model.value_object.Cpf;
import br.com.beauty_book.customer_management.domain.model.value_object.Email;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Customer {

    private final Long id;
    private final String name;
    private final Cpf cpf;
    private final String phoneNumber;
    private final Email email;

    public Customer(Long id, String name, Cpf cpf, String phoneNumber, Email email) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.cpf = Objects.requireNonNull(cpf, "CPF must not be null");
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
