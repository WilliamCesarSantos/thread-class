package br.ada.tech.turma1171;

import br.ada.tech.turma1171.repository.txt.impl.CustomerRepositoryImpl;
import br.ada.tech.turma1171.service.batch.impl.CsvCustomerImportBatchImpl;
import br.ada.tech.turma1171.service.batch.impl.CsvCustomerLoader;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {
        var executor = Executors.newScheduledThreadPool(10);
        var locker = new ReentrantLock();

        var repository = new CustomerRepositoryImpl("database");

        var service = new CsvCustomerImportBatchImpl(
                repository,
                new CsvCustomerLoader(),
                "integration"
        );
        executor.scheduleAtFixedRate(
                () -> {
                    try {
                        locker.lock();
                        service.process();
                    } finally {
                        locker.unlock();
                    }
                },
                0,
                1,
                TimeUnit.SECONDS
        );
    }
}
