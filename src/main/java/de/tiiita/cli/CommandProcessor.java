package de.tiiita.cli;

import de.tiiita.cli.exception.MissingAnnotationException;
import de.tiiita.cli.exception.NoArgumentFoundException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.Nullable;

class CommandProcessor {

  /**
   * Injects the parsed arguments into the command instance {@link #injectArguments(Object, List)}.
   * It then calls the super method of callable which executes the command implementation.
   * @param command the command instance
   * @param args the parsed command arguments.
   */
  void process(Object command, List<ArgumentModel> args) {
    injectArguments(command, args);
    try {
      ((Callable<?>) command).call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This gets the argument models and the command and injects the argument values
   * into every field in the command instance where the {@link Argument} annotation was found.
   * If there is no argument found but the argument is required {@link Argument#optional()}
   * the usage will be print.
   * If the value of the argument is null it will not inject anything into the declared variable,
   * which means the default value will stay.
   *
   * @param command the command instance
   * @param args the arguments models parsed by the parser.
   */
  private void injectArguments(Object command, List<ArgumentModel> args) {
    for (Field declaredField : command.getClass().getDeclaredFields()) {
      if (!declaredField.isAnnotationPresent(Argument.class)) {
        continue;
      }

      Argument argumentAnnotation = declaredField.getAnnotation(Argument.class);

      declaredField.setAccessible(true);
      try {
        ArgumentModel argByName = getArgByName(argumentAnnotation.name(), args);
        if (!argumentAnnotation.optional() && argByName == null) {
          System.out.println(CommandService.getUsage(command));
        }

        if (argByName != null && argByName.getValue() != null) {
          declaredField.set(this, argByName.getValue());
        }

      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

  }


  /**
   * This is just a simple method to get an argument by its name.
   * @param name the name the method should search for.
   * @param args the arguments the method should search in.
   * @return the found argument model or null if no name matched with the given args.
   */
  @Nullable
  private ArgumentModel getArgByName(String name, List<ArgumentModel> args) {
    for (ArgumentModel arg : args) {
      if (arg.getName().equalsIgnoreCase(name)) {
        return arg;
      }
    }

    return null;
  }

  /**
   * This simple method validates the command instance has the command annotation, so
   * we do not get unexplainable errors in the later process.
   * @param command the command instance.
   */
   void validateAnnotations(Object command) {
    if (!command.getClass().isAnnotationPresent(Command.class)) {
      throw new MissingAnnotationException("Command class has to have CliCommand.class annotation");
    }
  }
}
