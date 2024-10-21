package br.ada.tech.turma1171.repository.txt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractTextRepository<T> {

    protected String rootFolder;
    protected TextWriteConverter<T> writeConverter;
    protected TextReadConverter<T> readConverter;

    public AbstractTextRepository(String rootFolder,
                                  TextWriteConverter<T> writeConverter,
                                  TextReadConverter<T> readConverter
    ) {
        this.rootFolder = rootFolder;
        this.writeConverter = writeConverter;
        this.readConverter = readConverter;
    }

    protected T write(String fileName, T object) {
        try {
            var textValue = writeConverter.write(object);
            Files.write(
                    Paths.get(rootFolder, fileName),
                    List.of(textValue),
                    StandardOpenOption.CREATE_NEW
            );
            return object;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Optional<T> readFile(String fileName) {
        try {
            T object = null;
            if (Files.exists(Paths.get(rootFolder, fileName))) {
                String line = Files.lines(Paths.get(rootFolder, fileName))
                        .collect(Collectors.joining());
                object = readConverter.read(line);
            }
            return Optional.ofNullable(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Optional<T> delete(String fileName) {
        try {
            Optional<T> object = readFile(fileName);
            Files.deleteIfExists(Paths.get(rootFolder, fileName));
            return object;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
