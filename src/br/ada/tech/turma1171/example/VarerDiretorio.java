package br.ada.tech.turma1171.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VarerDiretorio {

    public static List<String> diretorios = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        // thread -> src -> br -> ada -> tech -> turma1171 -> Calcular.java, ContaBancaria.java
        // Cria uma estrutura que receba uma path e faça uma varedura
        // identificando pastas e arquivos existentes.
        // Lembre-se que para cada nova pasta identificada, deve-se iniciar
        // uma nova thread para varrer os arquivos existentes
        var path = Paths.get(".");
        varrer(path);
//        Thread.sleep(10);
        System.out.println("Main - Finalizando a aplicação");
    }

    public static void varrer(Path path) {
        if (!Files.isDirectory(path)) {
            System.out.println("Ação de varrer é valida apenas para diretórios");
            return;
        }
        var thread = new Thread(() -> {
            try {
                Files.list(path)
                        .peek(subPath -> diretorios.add(subPath.toAbsolutePath().toString()))
                        .forEach(subPath -> {
                            System.out.println("Thread " + Thread.currentThread().getName()
                                    + ". " + subPath.toAbsolutePath());
                            if (Files.isDirectory(subPath)) {
                                varrer(subPath);
                            }
                        });

                diretorios.forEach(it -> it.equalsIgnoreCase("a"));

                var threadName = Thread.currentThread().getName();
                System.out.println(threadName + " - Finalizando thread");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
//        thread.setDaemon(true);
        thread.start();
    }

}
