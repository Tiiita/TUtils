package de.tiiita;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;

public abstract class Collections {

    /**
     * With that function you can get a random object out of every collection.
     *
     * @param collection The collection where you want a random object from.
     * @return Returns a random object or null if the collection was empty.
     * @param <T> The object type.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getRandom(Collection<T> collection) {
        Object[] array = collection.toArray();

        switch (collection.size()) {
            case 0: return null;
            case 1: return (T) array[0];
            default: {
                int randomIndex = new Random().nextInt(collection.size());
                return (T) array[randomIndex];
            }
        }
    }
}
