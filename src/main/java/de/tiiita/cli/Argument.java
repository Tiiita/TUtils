package de.tiiita.cli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add this annotation over your field to mark it as argument.
 * The declared field will automatically have the value set, so it seems like an
 * always null field, but no. You can use this field in your code.
 * The value will just be null if the processor was not able to set the value
 * because it was not specified in the console input. It will then stay at its
 * default value (on declared fields null or the default value of the primitive type,
 * but you should always use string fields)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Argument {
  String name();
  boolean optional();
}
