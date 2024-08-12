package de.tiiita.json;

/**
 * An JsonElementBuilder is a builder which can build a specific json element.
 */
@FunctionalInterface
public interface JsonElementBuilder {

    /**
     * Calling this will build the added objects together and match the right json syntax.
     * @return the build json string.
     */
    String build();

}
