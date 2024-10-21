package br.ada.tech.turma1171.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class MainAsyncFile {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            exception.printStackTrace();
        });
//        Exercício com arquivos de mock.
//          - Criar uma forma de pesquisar uma palavra nos arquivos em paralelo
//          - Pesquisar qualquer parte do texto
//          - Lembre-se de que são 5 arquivos
//          - Deve juntar os resultados em uma lista mostrando em qual arquivo estava.
//             - Vicent
//                - 5,Vincent,Eason,veason4@feedburner.com -> MOCK_DATA_I.csv
//                - 906,Vincent,jose,jose@feedburner.com -> MOCK_DATA_I.csv
//                - 501,Vincent,Jhonson,jhonson@joao.com -> MOCK_DATA_IV.csv
        var executor = Executors.newSingleThreadExecutor();

        var result = executor.submit(() -> {
            Thread.sleep(1000);
            return "William";
        });
        while(!result.isDone()) {
            Thread.sleep(100);
            System.out.println(LocalDateTime.now()+": Aguardando");
        }
        System.out.println(result.get());

        System.out.println("Informe o texto que deseja buscar");
        var scanner = new Scanner(System.in);
        var text = scanner.nextLine();

        var diretorio = Paths.get("mocks");
        Files.list(diretorio)
                .filter(file -> file.getFileName().toString().endsWith(".csv"))
                .map(file ->
                        leituraEFiltro(
                                file,
                                (line) -> line.toLowerCase().contains(text.toLowerCase())
                        )
                ).forEach(executor::submit);

        executor.shutdown();
        System.out.println("Shutdown feito");
    }

    /*public static Runnable leituraEFiltro(Path file, Predicate<String> filtro) {
        return new Runnable() {
            @Override
            public void run() {

            }
        };
    }*/
    public static Runnable leituraEFiltro(Path file, Predicate<String> filtro) {
        return () -> {
            var fileName = file.getFileName().toString();
            try {
                Files.lines(file)
                        .filter(filtro)
                        .forEach(line ->
                                System.out.println(LocalDateTime.now() + ": " + line + " -> " + fileName)
                        );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
