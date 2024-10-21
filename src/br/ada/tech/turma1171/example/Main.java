package br.ada.tech.turma1171.example;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Olá mundo");

        final int index = 100;

        var thread1 = new Thread(() -> {
            for (int i = 0; i < index; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                System.out.println("Olá mundo");
            }
        });
        thread1.setName("Thread 1");

        var thread2 = new Thread(() -> {
            for (int i = 0; i < index; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                System.out.println("Hello world");
            }
        });
        thread2.setName("Thread 2");

        thread1.start();
        thread2.start();

        System.out.println(LocalDateTime.now()+": aguardando thread 1");
        thread1.join();
        System.out.println(LocalDateTime.now()+": aguardando thread 2");
        thread2.join();
        System.out.println(LocalDateTime.now()+": finalizado");
    }

}

