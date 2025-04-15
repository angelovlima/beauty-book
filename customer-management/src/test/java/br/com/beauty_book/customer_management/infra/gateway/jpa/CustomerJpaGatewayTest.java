package br.com.beauty_book.customer_management.infra.gateway.jpa;

import br.com.beauty_book.customer_management.domain.model.Customer;
import br.com.beauty_book.customer_management.domain.model.value_object.Cpf;
import br.com.beauty_book.customer_management.domain.model.value_object.Email;
import br.com.beauty_book.customer_management.infra.gateway.jpa.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CustomerJpaGateway.class)
class CustomerJpaGatewayTest {

    @Autowired
    private CustomerJpaGateway gateway;

    @Autowired
    private CustomerRepository repository;

    @Test
    @DisplayName("should save a new customer successfully")
    void shouldSaveCustomer() {
        Customer customer = new Customer(null, "Bruna Ferreira", new Cpf("123.456.789-00"), "1198888-8888", new Email("bruna@example.com"));

        Customer saved = gateway.save(customer);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Bruna Ferreira");
    }

    @Test
    @DisplayName("should find customer by ID")
    void shouldFindById() {
        Customer customer = new Customer(null, "João Pedro", new Cpf("111.222.333-44"), "1197777-7777", new Email("joao.pedro@example.com"));
        Customer saved = gateway.save(customer);

        Optional<Customer> found = gateway.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("João Pedro");
    }

    @Test
    @DisplayName("should find customer by CPF")
    void shouldFindByCpf() {
        Customer customer = new Customer(null, "Mariana Souza", new Cpf("555.666.777-88"), "1196666-6666", new Email("mariana@example.com"));
        gateway.save(customer);

        Optional<Customer> found = gateway.findByCpf("555.666.777-88");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mariana Souza");
    }

    @Test
    @DisplayName("should return all customers")
    void shouldReturnAll() {
        Customer c1 = new Customer(null, "Carlos Lima", new Cpf("999.888.777-66"), "1195555-5555", new Email("carlos.lima@example.com"));
        Customer c2 = new Customer(null, "Fernanda Martins", new Cpf("888.777.666-55"), "1194444-4444", new Email("fernanda@example.com"));

        gateway.save(c1);
        gateway.save(c2);

        List<Customer> all = gateway.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("should delete customer by ID")
    void shouldDeleteById() {
        Customer customer = new Customer(null, "Rafael Gomes", new Cpf("777.666.555-44"), "1193333-3333", new Email("rafael@example.com"));
        Customer saved = gateway.save(customer);

        gateway.deleteById(saved.getId());

        Optional<Customer> result = gateway.findById(saved.getId());

        assertThat(result).isEmpty();
    }
}
