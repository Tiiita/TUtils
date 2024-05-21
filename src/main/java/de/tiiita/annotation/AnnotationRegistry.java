package de.tiiita.annotation;

import java.util.Collection;
import java.util.HashSet;

public class AnnotationRegistry {

    private static final Collection<Object> registeredInstances = new HashSet<>();

    public AnnotationRegistry() {
        registerProcessor();
    }
    public void registerClass(Object instance) {
        registeredInstances.add(instance);
    }

    public void unregisterClass(Object instance) {
        registeredInstances.remove(instance);
    }

    protected Collection<Object> getRegisteredInstances() {
        return new HashSet<>(registeredInstances);
    }

    private void registerProcessor() {
      //Register annotation processors here
    }
}
