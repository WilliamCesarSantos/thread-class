package br.ada.tech.turma1171;

import java.util.concurrent.Executors;

public class MainAsync {

    public static void main(String[] args) {
        var executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            for (int index = 0; index < 49; index++) {
                System.out.println("Contador1 - Valor: " + index);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        });

        executor.submit(() -> {
            for (int index = 50; index < 99; index++) {
                System.out.println("Contador2 - Valor: " + index);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        });

        executor.shutdown();
    }

    /*
     *
     * Async: main ----------------| contador1 -------------------| contador2 ------------|
     *
     * Paralelo: main --------------------------------------|
     *                       | contador1 -------------------|
     *                       | contador2 -------------------|
     */

}
