package de.tiiita;

import java.util.HashSet;
import java.util.List;
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

    /**
     * We all know it... You iterate through a collection and want to get
     * the next object (.get(i + 1)). You always have to check if the size is
     * big enough to not through an {@link IndexOutOfBoundsException}.
     * <p></p>
     * This method tries to get the by the index. If this index is out of bounds
     * it will prevent the exception but will just return null.
     * This makes it easier because you just have to make a null check.
     *
     * @param collection the collection to search in.
     * @param index the current index. The method will try to add 1 to the index you pass in.
     * @return the next object or null if the index points already to the last object in the collection.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getOrNull(Collection<T> collection, int index) {
        Object[] array = collection.toArray();

        try {
            return (T) array[index];
        } catch (IndexOutOfBoundsException ignore) {
            return null;
        }
    }
}
