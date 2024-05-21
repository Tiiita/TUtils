package de.tiiita;

import de.tiiita.minecraft.util.MojangProfileFetcher;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public void start() {


        Instant startTime = Instant.now();
        String uuid = MojangProfileFetcher.fetchDataOrNull("Tiiita_");
        System.out.println(uuid + " (" + Duration.between(startTime, Instant.now()).toMillis() + " ms)");


    }
    public static void main(String[] args) {
        new Main().start();
    }
}