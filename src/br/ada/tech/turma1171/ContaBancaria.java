package br.ada.tech.turma1171;

public class ContaBancaria {

    private static Double saldo = 100d;

    public static void main(String[] args) throws InterruptedException {
        var thread1 = new Thread(() -> {
            var saldoAtual = saldo;
            saldoAtual = saldoAtual - 10;
            saldo = saldoAtual;
        });

        var thread2 = new Thread(() -> {
            saldo -= 15;
        });
        thread1.start();
        thread2.start();

        Thread.sleep(1000);

        System.out.println(saldo);
    }
}
