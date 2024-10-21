package br.ada.tech.turma1171.example;

import java.math.BigDecimal;
import java.util.Random;

public class Calcular {

    private static BigDecimal valor = BigDecimal.ONE;

    private static Random random = new Random();

    public static void main(String[] args) {
        Runnable calcular = () -> {
            synchronized (valor) {
                var valorAtual = valor;
                valor = valorAtual.add(BigDecimal.valueOf(random.nextDouble()));
                System.out.println("valor atual é: " + valorAtual);
                System.out.println("valor é: " + valor);
            }
        };
        var thread1 = new Thread(calcular);
        thread1.setName("Thread1");

        var thread2 = new Thread(calcular);
        thread2.setName("thread2");

        thread1.start();
        thread2.start();
    }

}
