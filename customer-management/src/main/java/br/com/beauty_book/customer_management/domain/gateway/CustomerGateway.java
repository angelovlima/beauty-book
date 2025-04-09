package br.com.beauty_book.customer_management.domain.gateway;

import br.com.beauty_book.customer_management.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerGateway {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    void deleteById(Long id);

    Optional<Customer> findByCpf(String cpf);

}
