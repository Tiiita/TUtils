package de.tiiita;

import de.tiiita.annotation.AnnotationRegistry;

public class Main {

    public void start() {
        new AnnotationRegistry();
    }
    public static void main(String[] args) {
        new Main().start();
    }
}