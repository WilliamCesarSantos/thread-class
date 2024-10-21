package br.ada.tech.turma1171.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.function.Predicate;

public class DesafioThread {

    public static void main(String[] args) throws IOException {
//        Exercício com arquivos de mock.
//          - Criar uma forma de pesquisar uma palavra nos arquivos em paralelo
//          - Pesquisar qualquer parte do texto
//          - Lembre-se de que são 5 arquivos
//          - Deve juntar os resultados em uma lista mostrando em qual arquivo estava.
//             - Vicent
//                - 5,Vincent,Eason,veason4@feedburner.com -> MOCK_DATA_I.csv
//                - 906,Vincent,jose,jose@feedburner.com -> MOCK_DATA_I.csv
//                - 501,Vincent,Jhonson,jhonson@joao.com -> MOCK_DATA_IV.csv
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
                ).forEach(Thread::start);
    }

    public static Thread leituraEFiltro(Path file, Predicate<String> filtro) {
        return new Thread(() -> {
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

        });
    }

}
