package br.ada.tech.turma1171.service.batch.impl;

import br.ada.tech.turma1171.model.Customer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CsvCustomerLoader {

    public List<Customer> load(Path path) {
        try {
            return Files.lines(path)
                    .parallel()
                    .map(this::convert)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Customer> convert(String text) {
        Customer customer = null;
        var values = text.split(",");
        if (values.length == 3) {
            var id = Long.valueOf(values[0]);
            var name = values[1];
            var birtDate = LocalDate.parse(values[2]);
            customer = new Customer(id, name, birtDate);
        }
        return Optional.ofNullable(customer);
    }

}
