package de.tiiita.annotation;

import de.tiiita.annotation.asynconly.AsyncOnlyProcessor;


/**
 * A class to register any processor for annotations. Just to not overuse the main method.
 */
public class AnnotationRegistry {
    public AnnotationRegistry() {
        new AsyncOnlyProcessor();
    }
}
