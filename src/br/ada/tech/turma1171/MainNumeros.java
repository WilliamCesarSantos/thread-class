package br.ada.tech.turma1171;

public class MainNumeros {

    public static void main(String[] args) {
        // Cria duas thread para realizar a contagem de 0..100
        // Cada thread deve contar métade dos números. Ou seja, uma conta de 0..49 e outra de 50..99
        // Lembre-se de imprimir os números no console.
        // Adicione um sleep de 10 mili a cada número contado
        var contador1 = new Thread(() -> {
            for (int index = 0; index < 49; index++) {
                System.out.println("Contador1 - Valor: " + index);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        });

        var contador2 = new Thread(() -> {
            for (int index = 50; index < 99; index++) {
                System.out.println("Contador2 - Valor: " + index);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        });

        contador1.start();
        contador2.start();
    }
}
