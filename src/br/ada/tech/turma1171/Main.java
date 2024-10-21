package br.ada.tech.turma1171;

import br.ada.tech.turma1171.model.Customer;
import br.ada.tech.turma1171.repository.txt.impl.CustomerRepositoryImpl;
import br.ada.tech.turma1171.service.batch.impl.CsvCustomerImportBatchImpl;
import br.ada.tech.turma1171.service.batch.impl.CsvCustomerLoader;

public class Main {

    public static void main(String[] args) {
        var repository = new CustomerRepositoryImpl("database");

        var service = new CsvCustomerImportBatchImpl(
                repository,
                new CsvCustomerLoader(),
                "integration"
        );
        service.process();
    }
}
