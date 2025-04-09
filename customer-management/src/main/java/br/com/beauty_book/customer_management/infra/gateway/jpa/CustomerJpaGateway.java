package br.com.beauty_book.customer_management.infra.gateway.jpa;

import br.com.beauty_book.customer_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.customer_management.domain.model.Customer;
import br.com.beauty_book.customer_management.infra.gateway.jpa.mapper.CustomerJpaMapper;
import br.com.beauty_book.customer_management.infra.gateway.jpa.repository.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomerJpaGateway implements CustomerGateway {

    private final CustomerRepository repository;

    public CustomerJpaGateway(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {
        var entity = CustomerJpaMapper.toEntity(customer);
        var savedEntity = repository.save(entity);
        return CustomerJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return repository.findById(id)
                .map(CustomerJpaMapper::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll().stream()
                .map(CustomerJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Customer> findByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(CustomerJpaMapper::toDomain);
    }
}
