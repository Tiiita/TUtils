package de.tiiita.shell;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation that must be put over the
 * class declaration to mark this class as command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

  /**
   * The name of the command. This is the string you later call the command in the console.
   * @return the name set in the command impl.
   */
  String name();

  /**
   * The command description. This will be shown in the help command for example
   * to explain what the command does.
   * @return the set description or a default one.
   */
  String description() default "No description set..";
}
