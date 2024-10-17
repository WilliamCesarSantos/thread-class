package br.ada.tech.turma1171;

import java.time.LocalDateTime;

public class EnviarMensagem {

    private static boolean mensagemEnviada = false;

    public static void main(String[] args) throws InterruptedException {
        final var mainThread = Thread.currentThread();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Voltou do sleep e vai alterar a variavel");
                mensagemEnviada = true;
                mainThread.interrupt();
            } catch (InterruptedException e) {
                System.out.println("Thread interrompida");
            }
        }).start();

        while (!mensagemEnviada) {
            try {
                System.out.println("Enviando mensagem..." + LocalDateTime.now());
                Thread.sleep(756);
            } catch (InterruptedException ex) {
                System.out.println("Interrupt - Voltou. " + LocalDateTime.now());
            }
        }
        System.out.println("Mensagem enviada." + LocalDateTime.now());
    }

}
