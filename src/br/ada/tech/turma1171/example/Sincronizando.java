package br.ada.tech.turma1171.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Sincronizando {

    public static void main(String[] args) {
        var barrier = new CyclicBarrier(2);

        Runnable runnable = () -> {
            try {
                System.out.println("Passo 1");
                barrier.await();
                System.out.println("Passo 2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
    }

}
