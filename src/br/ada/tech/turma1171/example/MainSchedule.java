package br.ada.tech.turma1171.example;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainSchedule {

    public static void main(String[] args) {
        var executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> System.out.println(LocalDateTime.now()+": Executou");

        System.out.println(LocalDateTime.now()+": iniciando");
        //executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(task, 5, 1, TimeUnit.SECONDS);
    }

}
