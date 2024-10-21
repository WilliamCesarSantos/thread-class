package br.ada.tech.turma1171.service.batch.impl;

import br.ada.tech.turma1171.repository.CustomerRepository;
import br.ada.tech.turma1171.service.CustomerImportBatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class CsvCustomerImportBatchImpl implements CustomerImportBatch {

    private CustomerRepository repository;
    private CsvCustomerLoader loader;
    private String rootFolder;
    private String processed = "/processed";

    public CsvCustomerImportBatchImpl(
            CustomerRepository repository,
            CsvCustomerLoader loader,
            String rootFolder
    ) {
        this.repository = repository;
        this.loader = loader;
        this.rootFolder = rootFolder;
    }

    @Override
    public void process() {
        filesToProcess()
                .forEach(file -> {
                    loader.load(file)
                            .parallelStream()
                            .filter(customer ->
                                    !repository.findById(customer.getId()).isPresent()
                            ).forEach(repository::save);
                    markAsProcessed(file);
                });
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
