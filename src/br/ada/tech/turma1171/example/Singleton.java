package br.ada.tech.turma1171.example;

public class Singleton {

    private static final Singleton INSTANCE = new Singleton();


    public static Singleton getInstance() {
        return INSTANCE;
    }

}
