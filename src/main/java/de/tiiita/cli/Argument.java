package de.tiiita.cli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add this annotation over your field to mark it as argument. The declared field will automatically
 * have the value set, so it seems like an always null field, but no. You can use this field in your
 * code. The value will just be null if the processor was not able to set the value because it was
 * not specified in the console input. It will then stay at its default value (on declared fields
 * null or the default value of the primitive type, but you should always use string fields)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Argument {

  String name();

  boolean optional();


  /**
   * A normal argument is used to pass information from the cli to the code. But what if you just
   * want to pass in an argument and want to check if this argument is present or not?
   * <p>
   * Here a little example:
   * <code>command -arg argValue</code> <p></p>
   * This code gives the argValue to the code using the -arg argument. But what if I only want to
   * check if the -arg argument is present and do not want to give information? For that case the
   * valued option comes in. When this setting is set to true the parser expects and argument value,
   * if no value was passed in it will set the field to null.
   * <p>
   * If this is false the field needs to be a boolean! The boolean will be set to true if the -arg
   * argument was found by the parser. If not it will be false.
   * <p><p>
   * This feature provides another option to design your cli commands.
   */
  boolean valued();
}
