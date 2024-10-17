package br.ada.tech.turma1171;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VarerDiretorio {

    public static void main(String[] args) throws InterruptedException {
        // thread -> src -> br -> ada -> tech -> turma1171 -> Calcular.java, ContaBancaria.java
        // Cria uma estrutura que receba uma path e faça uma varedura
        // identificando pastas e arquivos existentes.
        // Lembre-se que para cada nova pasta identificada, deve-se iniciar
        // uma nova thread para varrer os arquivos existentes
        var path = Paths.get(".");
        varrer(path);
        Thread.sleep(10);
    }

    public static void varrer(Path path) {
        if (!Files.isDirectory(path)) {
            System.out.println("Ação de varrer é valida apenas para diretórios");
            return;
        }
        var thread = new Thread(() -> {
            try {
                Files.list(path)
                        .forEach(subPath -> {
                            System.out.println("Thread " + Thread.currentThread().getName()
                                    + ". " + subPath.toAbsolutePath());
                            if (Files.isDirectory(subPath)) {
                                varrer(subPath);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

}
