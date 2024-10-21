package br.ada.tech.turma1171.repository;

import br.ada.tech.turma1171.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    Optional<Customer> deleteById(Long id);

}
