package br.ada.tech.turma1171.service.batch.impl;

import br.ada.tech.turma1171.repository.CustomerRepository;
import br.ada.tech.turma1171.service.CustomerImportBatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class CsvCustomerImportBatchImpl implements CustomerImportBatch {

    private CustomerRepository repository;
    private CsvCustomerLoader loader;
    private String rootFolder;
    private String processed = "/processed";
    private Map<String, Lock> lockers;

    public CsvCustomerImportBatchImpl(
            CustomerRepository repository,
            CsvCustomerLoader loader,
            String rootFolder
    ) {
        this.repository = repository;
        this.loader = loader;
        this.rootFolder = rootFolder;
        this.lockers = new ConcurrentHashMap<>();
    }

    @Override
    public void process() {
        // Desafio. Criar zona de exclusão para garantir que apenas uma thread possa executar o
        // processamento do arquivo.
        System.out.println(LocalDateTime.now() + ": Executando integração de usuários");
        filesToProcess()
                .parallel()
                .forEach(file -> {
                    var fileName = file.toAbsolutePath().toString();
                    var lock = lockers.get(fileName);
                    if (lock == null) {
                        lock = new ReentrantLock();
                        lockers.put(fileName, lock);
                    }
                    try {
                        lock = lockers.get(fileName);
                        if (lock.tryLock()
                                && Files.exists(file)) {
                            processFile(file);
                            lockers.remove(fileName);
                        } else {
                            System.out.println("Descartando arquivo por falta de lock");
                        }
                    } finally {
                        lock.unlock();
                    }
                });
    }

    private void processFile(Path file) {
        loader.load(file)
                .parallelStream()
                .filter(customer -> {
                    var exists = repository.findById(customer.getId()).isPresent();
                    if (exists) {
                        System.out.println("Descartando usuário " + customer.getName() + ", pois já existe na base de dados");
                    }
                    return !exists;
                }).forEach(repository::save);
        markAsProcessed(file);
    }

    private void markAsProcessed(Path file) {
        try {
            Files.move(
                    file,
                    Paths.get(rootFolder, processed + "/" + file.getFileName().toString()),
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Path> filesToProcess() {
        try {
            var path = Paths.get(rootFolder);
            return Files.list(path)
                    .filter(it -> it.getFileName().toString().endsWith(".csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
