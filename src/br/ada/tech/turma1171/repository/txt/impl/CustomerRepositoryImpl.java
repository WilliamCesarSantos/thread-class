package br.ada.tech.turma1171.repository.txt.impl;

import br.ada.tech.turma1171.model.Customer;
import br.ada.tech.turma1171.repository.CustomerRepository;
import br.ada.tech.turma1171.repository.txt.AbstractTextRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerRepositoryImpl
        extends AbstractTextRepository<Customer>
        implements CustomerRepository {

    private static Long lastId = 0l;

    private final String extension = ".txt";

    public CustomerRepositoryImpl(String rootFolder) {
        super(
                rootFolder + "/txt/customers",
                (customer) ->
                        String.format(
                                "%1$s|%2$s|%3$s",
                                customer.getId(),
                                customer.getName(),
                                customer.getBirthDate()
                        ),
                (text) -> {
                    var values = text.split("\\|");
                    var id = Long.valueOf(values[0]);
                    var name = values[1];
                    var birtDate = LocalDate.parse(values[2]);
                    return new Customer(id, name, birtDate);
                }
        );
        discoveryLastId();
    }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            synchronized (CustomerRepositoryImpl.lastId) {
                var lastId = CustomerRepositoryImpl.lastId++;
                customer.setId(lastId);
            }
        }
        System.out.println(LocalDateTime.now()+": Registrando cliente "+customer.getName());
        super.write(
                customer.getId() + extension,
                customer
        );
        return customer;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return readFile(id + extension);
    }

    @Override
    public List<Customer> findAll() {
        try {
            return Files.list(Paths.get(rootFolder))
                    .map(file -> file.getFileName().toString())
                    .filter(fileName -> fileName.endsWith(extension))
                    .map(super::readFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Customer> deleteById(Long id) {
        return delete(id + extension);
    }

    private void discoveryLastId() {
        try {
            synchronized (CustomerRepositoryImpl.lastId) {
                var path = Paths.get(rootFolder);
                var lastId = Files.list(path)
                        .map(subPath -> subPath.getFileName().toString())
                        .filter(fileName -> fileName.endsWith(extension))
                        .map(fileName -> Long.valueOf(fileName.split("\\.")[0]))
                        .reduce((first, second) -> first > second ? first : second)
                        .orElse(0l);
                CustomerRepositoryImpl.lastId = lastId;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
